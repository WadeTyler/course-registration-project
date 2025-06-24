import type {Course} from "../../../../../../types/course.types.ts";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {type FormEvent, useEffect, useState} from "react";
import type {ManagePrerequisiteRequest, Prerequisite} from "../../../../../../types/prerequisite.types.ts";
import {updatePrerequisite} from "../../../../../../features/prerequisite/prerequisite.api.ts";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "../../../../../../components/ui/sheet.tsx";
import {Button} from "../../../../../../components/ui/button.tsx";
import {Label} from "../../../../../../components/ui/label.tsx";
import {Input} from "../../../../../../components/ui/input.tsx";
import Loader from "../../../../../../components/Loader.tsx";

export default function EditPrerequisiteSheet({course, prerequisite}: {course: Course, prerequisite: Prerequisite}) {
  const queryClient = useQueryClient();

  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<ManagePrerequisiteRequest>({
    requiredCourseId: prerequisite.requiredCourseId,
    minimumGrade: prerequisite.minimumGrade
  });

  function resetFormData() {
    setFormData({
      requiredCourseId: prerequisite.requiredCourseId,
      minimumGrade: prerequisite.minimumGrade
    });
  }

  useEffect(() => {
    resetFormData();
  }, [open]);

  const {mutate: updateMutation, isPending: isUpdating, error: updateError} = useMutation({
    mutationFn: updatePrerequisite,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['course']});
      await queryClient.invalidateQueries({queryKey: ['courses']});
      setOpen(false);
    }
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (isUpdating) return;
    updateMutation({courseId: course.id, managePrerequisiteRequest: formData, prerequisiteId: prerequisite.id});
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button>Edit Prerequisite</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Edit Prerequisite</SheetTitle>
          <SheetDescription>Edit a prerequisite for {course.department}-{course.code}.</SheetDescription>
        </SheetHeader>
        <form className="w-full flex flex-col gap-4 p-4" onSubmit={handleSubmit}>

          <div className="flex-col-2">
            <Label htmlFor="minimumGrade">Minimum Grade</Label>
            <Input
              type="number"
              placeholder="Enter a minimum grade."
              min={0}
              max={100}
              required
              value={formData.minimumGrade}
              onChange={e => setFormData(prev => ({
                ...prev,
                minimumGrade: e.target.valueAsNumber
              }))}
            />
          </div>

          {updateError && (
            <p className="text-destructive text-center text-balance">
              {(updateError as Error).message}
            </p>
          )}

          <Button type="submit" disabled={isUpdating}>
            {isUpdating ? <Loader/> : "Save Changes"}
          </Button>
        </form>
      </SheetContent>
    </Sheet>
  )
}
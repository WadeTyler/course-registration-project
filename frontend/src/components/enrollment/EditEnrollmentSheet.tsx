import type {Enrollment, ManageEnrollmentRequest} from "../../types/enrollment.types.ts";
import {Sheet, SheetContent, SheetDescription, SheetHeader, SheetTitle, SheetTrigger} from "../ui/sheet.tsx";
import {type FormEvent, useState} from "react";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {Button} from "../ui/button.tsx";
import {Label} from "../ui/label.tsx";
import {Input} from "../ui/input.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "../ui/select.tsx";
import Loader from "../Loader.tsx";
import {updateEnrollment} from "../../features/enrollment/enrollment.api.ts";

export default function EditEnrollmentSheet({enrollment}: { enrollment: Enrollment }) {
  const queryClient = useQueryClient();

  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<ManageEnrollmentRequest>({
    grade: enrollment.grade,
    status: enrollment.status
  });

  const course = enrollment.courseSection.course;

  const {mutate: updateMutation, isPending: isUpdating, error: updateError} = useMutation({
    mutationFn: updateEnrollment,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['enrollments']});
      setOpen(false);
    }
  })

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (isUpdating) return;
    updateMutation({
      studentId: enrollment.student.id,
      courseSectionId: enrollment.courseSection.id,
      manageEnrollmentRequest: formData
    });

  }


  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button>Edit</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Edit Enrollment</SheetTitle>
          <SheetDescription>
            Edit enrollment
            for {enrollment.student.firstName} in {course.department}-{course.code}-{enrollment.courseSection.id}.
          </SheetDescription>
        </SheetHeader>
        <form onSubmit={handleSubmit} className="w-full flex flex-col gap-4 p-4">
          {/* Grade */}
          <div className="flex-col-2">
            <Label htmlFor="grade">Grade</Label>
            <Input
              id="grade"
              type="number"
              min={0}
              max={100}
              required
              placeholder="Enter grade"
              value={formData.grade}
              onChange={e => setFormData(prev => ({
                ...prev, grade: e.target.valueAsNumber
              }))}
            />
          </div>

          {/* Status */}
          <div className="flex-col-2">
            <Label htmlFor="status">Status</Label>
            <Select required value={formData.status} onValueChange={e => setFormData(prev => ({
              ...prev, status: e
            }))}>
              <SelectTrigger className="w-full">
                <SelectValue id="status" placeholder="Choose status"/>
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="NOT_STARTED">NOT STARTED</SelectItem>
                <SelectItem value="STARTED">STARTED</SelectItem>
                <SelectItem value="COMPLETED">COMPLETED</SelectItem>
                <SelectItem value="DROPPED">DROPPED</SelectItem>
                <SelectItem value="FAILED">FAILED</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {updateError && (
            <p className="text-destructive text-center text-balance">{(updateError as Error).message}</p>
          )}

          <Button type="submit" disabled={isUpdating}>
            {isUpdating ? <Loader/> : "Save Changes"}
          </Button>
        </form>

      </SheetContent>

    </Sheet>
  )
}
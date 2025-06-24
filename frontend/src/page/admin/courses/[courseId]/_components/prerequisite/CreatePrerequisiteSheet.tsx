import type {Course} from "../../../../../../types/course.types.ts";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "../../../../../../components/ui/sheet.tsx";
import {type FormEvent, useEffect, useState} from "react";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {Button} from "../../../../../../components/ui/button.tsx";
import type {ManagePrerequisiteRequest} from "../../../../../../types/prerequisite.types.ts";
import {createPrerequisite} from "../../../../../../features/prerequisite/prerequisite.api.ts";
import {Label} from "../../../../../../components/ui/label.tsx";
import {Popover, PopoverContent, PopoverTrigger} from "../../../../../../components/ui/popover.tsx";
import {getAllCourses} from "../../../../../../features/course/course.api.ts";
import {ChevronsUpDown} from "lucide-react";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList
} from "../../../../../../components/ui/command.tsx";
import {Input} from "../../../../../../components/ui/input.tsx";
import Loader from "../../../../../../components/Loader.tsx";

export default function CreatePrerequisiteSheet({course}: { course: Course }) {
  const queryClient = useQueryClient();

  const {data: courses} = useQuery({
    queryKey: ['courses'],
    queryFn: getAllCourses
  });

  const [open, setOpen] = useState(false);
  const [requiredCourseOpen, setRequiredCourseOpen] = useState(false);
  const [formData, setFormData] = useState<ManagePrerequisiteRequest>({
    requiredCourseId: 0,
    minimumGrade: 0
  });

  function resetFormData() {
    setFormData({
      requiredCourseId: 0,
      minimumGrade: 0
    });
  }

  useEffect(() => {
    resetFormData();
  }, [open]);

  const {mutate: createMutation, isPending: isCreating, error: createError} = useMutation({
    mutationFn: createPrerequisite,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['course']});
      await queryClient.invalidateQueries({queryKey: ['courses']});
      setOpen(false);
    }
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (isCreating) return;
    createMutation({courseId: course.id, managePrerequisiteRequest: formData});
  }

  function getCourseLabel(id: number) {
    const targetCourse = courses?.find(course => course.id === id);
    if (!targetCourse) return "Course not found.";
    return targetCourse?.department + "-" + targetCourse?.code;
  }

  function onRequiredCourseSelected(depCode: string) {

    const targetCourse = courses?.find(course => course.department + "-" + course.code === depCode);
    setFormData(prev => ({
      ...prev,
      requiredCourseId: targetCourse ? targetCourse.id : 0
    }));
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button>Create Prerequisite</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Create Prerequisite</SheetTitle>
          <SheetDescription>Create a prerequisite for {course.department}-{course.code}.</SheetDescription>
        </SheetHeader>
        <form className="w-full flex flex-col gap-4 p-4" onSubmit={handleSubmit}>

          {/* Required Course */}
          <div className="flex-col-2">
            <Label>Required Course</Label>
            <Popover open={requiredCourseOpen} onOpenChange={setRequiredCourseOpen}>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  role="combobox"
                  aria-expanded={open}
                  className="w-[200px] justify-between"
                >
                  {formData.requiredCourseId !== 0 ? getCourseLabel(formData.requiredCourseId) : "Choose a course..."}
                  <ChevronsUpDown className="opacity-50"/>
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-full p-0">
                <Command>
                  <CommandInput placeholder="Search framework..." className="h-9"/>
                  <CommandList>
                    <CommandEmpty>No course found.</CommandEmpty>
                    <CommandGroup>
                      {courses?.filter(c => c.id !== course.id)
                        .map(c => (
                          <CommandItem
                            key={c.id}
                            value={c.department + "-" + c.code}
                            onSelect={(val) => {
                              onRequiredCourseSelected(val);
                              setRequiredCourseOpen(false);
                            }}
                          >
                            {c.department}-{c.code}
                          </CommandItem>
                        ))}
                    </CommandGroup>
                  </CommandList>
                </Command>
              </PopoverContent>
            </Popover>
          </div>

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

          {createError && (
            <p className="text-destructive text-center text-balance">
              {(createError as Error).message}
            </p>
          )}

          <Button type="submit" disabled={isCreating}>
            {isCreating ? <Loader/> : "Create Prerequisite"}
          </Button>
        </form>
      </SheetContent>
    </Sheet>
  )
}
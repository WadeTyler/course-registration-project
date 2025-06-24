import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "../../../../../../components/ui/sheet.tsx";
import type {Course, ManageCourseRequest} from "../../../../../../types/course.types.ts";
import {Button} from "../../../../../../components/ui/button.tsx";
import {type FormEvent, useEffect, useState} from "react";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {updateCourse} from "../../../../../../features/course/course.api.ts";
import {Label} from "../../../../../../components/ui/label.tsx";
import {Input} from "../../../../../../components/ui/input.tsx";
import {Textarea} from "../../../../../../components/ui/textarea.tsx";
import Loader from "../../../../../../components/Loader.tsx";

type Props = {
  course: Course;
}

export default function EditCourseSheet({course}: Props) {
  const queryClient = useQueryClient();

  // States
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<ManageCourseRequest>({
    department: course.department,
    code: course.code,
    title: course.title,
    description: course.description,
    credits: course.credits
  });

  function resetForm() {
    setFormData({
      department: course.department,
      code: course.code,
      title: course.title,
      description: course.description,
      credits: course.credits
    });
  }

  useEffect(() => {
    resetForm();
  }, [open]);

  const {mutate: updateCourseMutation, isPending: isUpdatingCourse, error: updateCourseError} = useMutation({
    mutationFn: updateCourse,
    onSuccess: async (updatedCourse) => {
      queryClient.setQueryData(['course'], updatedCourse);
      setOpen(false);
    }
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();

    if (isUpdatingCourse) return;
    updateCourseMutation({courseId: course.id, manageCourseRequest: formData});
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button>Edit Course</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Edit Course</SheetTitle>
          <SheetDescription>Edit Course Information.</SheetDescription>
        </SheetHeader>
        <form className="w-full flex flex-col gap-4 p-4" onSubmit={handleSubmit}>
          {/* Department */}
          <div className="flex-col-2">
            <Label htmlFor="department">Department</Label>
            <Input
              id="department"
              placeholder="Enter a department"
              value={formData.department}
              onChange={e => setFormData(prev => ({
                ...prev, department: e.target.value
              }))}
            />
          </div>

          {/* Code */}
          <div className="flex-col-2">
            <Label htmlFor="code">Code</Label>
            <Input
              id="code"
              type="number"
              placeholder="Enter a code"
              value={formData.code}
              onChange={e => setFormData(prev => ({
                ...prev, code: e.target.valueAsNumber
              }))}
            />
          </div>

          {/* Title */}
          <div className="flex-col-2">
            <Label htmlFor="department">Title</Label>
            <Input
              id="title"
              placeholder="Enter a title"
              value={formData.title}
              onChange={e => setFormData(prev => ({
                ...prev, title: e.target.value
              }))}
            />
          </div>

          {/* Description */}
          <div className="flex-col-2">
            <Label htmlFor="department">Description</Label>
            <Textarea
              id="description"
              placeholder="Enter a description"
              value={formData.description}
              onChange={e => setFormData(prev => ({
                ...prev, description: e.target.value
              }))}
            />
          </div>

          {/* Credits */}
          <div className="flex-col-2">
            <Label htmlFor="credits">Credits</Label>
            <Input
              id="credits"
              type="number"
              placeholder="Enter credits"
              value={formData.credits}
              onChange={e => setFormData(prev => ({
                ...prev, credits: e.target.valueAsNumber
              }))}
            />
          </div>

          {updateCourseError && (
            <p className="text-destructive text-center text-balance">{(updateCourseError as Error).message}</p>
          )}

          <Button type="submit">
            {isUpdatingCourse ? <Loader /> : "Save Changes"}
          </Button>
        </form>
      </SheetContent>

    </Sheet>
  )
}
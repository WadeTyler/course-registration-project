import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "../../../../components/ui/sheet.tsx";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {type FormEvent, useEffect, useState} from "react";
import type {ManageCourseRequest} from "../../../../types/course.types.ts";
import {createCourse} from "../../../../features/course/course.api.ts";
import {Button} from "../../../../components/ui/button.tsx";
import {Label} from "../../../../components/ui/label.tsx";
import {Input} from "../../../../components/ui/input.tsx";
import {Textarea} from "../../../../components/ui/textarea.tsx";
import Loader from "../../../../components/Loader.tsx";
import {useNavigate} from "react-router-dom";

export default function CreateCourseSheet() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  // States
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<ManageCourseRequest>({
    department: "",
    code: 0,
    title: "",
    description: "",
    credits: 0
  });

  function resetForm() {
    setFormData({
      department: "",
      code: 0,
      title: "",
      description: "",
      credits: 0
    });
  }

  useEffect(() => {
    resetForm();
  }, [open]);

  const {mutate: createCourseMutation, isPending: isCreating, error: createError} = useMutation({
    mutationFn: createCourse,
    onSuccess: async (course) => {
      await queryClient.invalidateQueries({queryKey: ['courses']});
      navigate(`/admin/courses/${course.id}`)
      setOpen(false);
    }
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();

    if (isCreating) return;
    createCourseMutation(formData);
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button>Create Course</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Create Course</SheetTitle>
          <SheetDescription>Create a new course.</SheetDescription>
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

          {createError && (
            <p className="text-destructive text-center text-balance">{(createError as Error).message}</p>
          )}

          <Button type="submit" disabled={isCreating}>
            {isCreating ? <Loader /> : "Create Course"}
          </Button>
        </form>
      </SheetContent>

    </Sheet>
  )
}
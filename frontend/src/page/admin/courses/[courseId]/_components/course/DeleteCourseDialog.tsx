import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "../../../../../../components/ui/dialog.tsx";
import type {Course} from "../../../../../../types/course.types.ts";
import {Button} from "../../../../../../components/ui/button.tsx";
import {useState} from "react";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {deleteCourse} from "../../../../../../features/course/course.api.ts";
import {useNavigate} from "react-router-dom";
import Loader from "../../../../../../components/Loader.tsx";

export default function DeleteCourseDialog({course}: { course: Course }) {

  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const [open, setOpen] = useState(false);

  const {mutate: deleteCourseMutation, isPending: isDeleting, error: deleteError} = useMutation({
    mutationFn: deleteCourse,
    onSuccess: () => {
      queryClient.invalidateQueries({queryKey: ['course']});
      navigate("/admin/courses");
    }
  })

  function handleDelete() {
    if (isDeleting) return;
    deleteCourseMutation({courseId: course.id});
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="destructive">
          Delete Course
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Delete Course</DialogTitle>
          <DialogDescription>
            You are about to delete the course: <b>{course.department}-{course.code}</b> {course.title}. This action is
            irreversible. Are you sure you want to do this?
          </DialogDescription>
        </DialogHeader>
        {deleteError && (
          <p className="text-destructive text-center text-balance">{(deleteError as Error).message}</p>
        )}
        <DialogFooter className="w-full flex items-center gap-4 justify-end">
          <Button variant="outline" onClick={() => setOpen(false)} disabled={isDeleting}>Never mind</Button>
          <Button variant="destructive" onClick={handleDelete} disabled={isDeleting}>
            {isDeleting ? <Loader/> : "Delete"}
          </Button>
        </DialogFooter>
      </DialogContent>


    </Dialog>
  )
}
import type {Enrollment} from "@/types/enrollment.types.ts";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "../ui/dialog.tsx";
import {useState} from "react";
import {Button} from "../ui/button.tsx";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {deleteEnrollment} from "@/features/enrollment/enrollment.api.ts";

export default function DropEnrollmentDialog({enrollment}: { enrollment: Enrollment }) {

  const queryClient = useQueryClient();
  const [open, setOpen] = useState(false);

  const course = enrollment.courseSection.course;

  const {mutate: deleteMutation, isPending: isDeleting, error: deleteError} = useMutation({
    mutationFn: deleteEnrollment,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['enrollments']});
      setOpen(false);
    }
  });

  function handleDelete() {
    if (isDeleting) return;
    deleteMutation({studentId: enrollment.student.id, courseSectionId: enrollment.courseSection.id});
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant={"destructive"}>Drop</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Drop Enrollment</DialogTitle>

          <DialogDescription>
            You are about to drop the enrollment in
            <b>{course.department}-{course.code}-{enrollment.courseSection.id}</b>. This action is irreversible. Are you
            sure you want to do this?
          </DialogDescription>
        </DialogHeader>

        {deleteError && (
          <p className="text-destructive text-center text-balance">{(deleteError as Error).message}</p>
        )}

        <DialogFooter className="w-full flex items-center justify-end gap-4">
          <Button onClick={() => setOpen(false)}
                  disabled={isDeleting} variant="outline">
            Never mind
          </Button>
          <Button variant={"destructive"} disabled={isDeleting} onClick={handleDelete}>
            Delete
          </Button>
        </DialogFooter>
      </DialogContent>

    </Dialog>
  )
}
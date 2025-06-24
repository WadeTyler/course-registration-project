import type {InstructorEnrollment} from "../../../../../types/enrollment.types.ts";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {useState} from "react";
import {deleteEnrollment} from "../../../../../features/enrollment/enrollment.api.ts";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "../../../../../components/ui/dialog.tsx";
import {Button} from "../../../../../components/ui/button.tsx";

export default function DropInstructorEnrollmentDialog({enrollment}: {enrollment: InstructorEnrollment}) {

  const queryClient = useQueryClient();
  const [open, setOpen] = useState(false);

  const {mutate: deleteMutation, isPending: isDeleting, error: deleteError} = useMutation({
    mutationFn: deleteEnrollment,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['assignedSection']});
      setOpen(false);
    }
  });

  function handleDelete() {
    if (isDeleting) return;
    deleteMutation({studentId: enrollment.student.id, courseSectionId: enrollment.courseSectionId});
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
            You are about to drop {enrollment.student.firstName} {enrollment.student.lastName}'s enrollment. Are you
            sure you want to do that?
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
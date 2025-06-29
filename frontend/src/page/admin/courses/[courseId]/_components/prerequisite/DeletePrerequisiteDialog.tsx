import {useMutation, useQueryClient} from "@tanstack/react-query";
import {useState} from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "@/components/ui/dialog.tsx";
import {Button} from "@/components/ui/button.tsx";
import {TrashIcon} from "lucide-react";
import Loader from "../../../../../../components/Loader.tsx";
import type {Prerequisite} from "@/types/prerequisite.types.ts";
import type {Course} from "@/types/course.types.ts";
import {deletePrerequisite} from "@/features/prerequisite/prerequisite.api.ts";

export default function DeletePrerequisiteDialog({prerequisite, course}: {
  prerequisite: Prerequisite,
  course: Course
}) {
  const queryClient = useQueryClient();

  const [open, setOpen] = useState(false);

  const {mutate: deleteMutation, isPending: isDeleting, error: deleteError} = useMutation({
    mutationFn: deletePrerequisite,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['course']});
    }
  });

  function handleDelete() {
    if (isDeleting) return;
    deleteMutation({courseId: prerequisite.courseId, prerequisiteId: prerequisite.id});
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="destructive" className="w-fit">
          <TrashIcon/>
          Delete Prerequisite
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Delete Prerequisite</DialogTitle>
          <DialogDescription>
            You are about to delete the <b>{prerequisite.requiredCourseDepartment}-{prerequisite.requiredCourseCode}</b> prerequisite for {course.department}-{course.code}. This action is irreversible. Are you sure you want to do
            this?
          </DialogDescription>
        </DialogHeader>

        {deleteError && (
          <p className="text-center text-destructive text-balance">{(deleteError as Error).message}</p>
        )}

        <DialogFooter className="w-full flex items-center gap-4 justify-end">
          <Button variant="outline" onClick={() => setOpen(false)}
                  disabled={isDeleting}
          >
            Never mind
          </Button>
          <Button variant="destructive" onClick={handleDelete} disabled={isDeleting}>
            {isDeleting ? <Loader/> : "Delete"}
          </Button>
        </DialogFooter>
      </DialogContent>

    </Dialog>
  )
}
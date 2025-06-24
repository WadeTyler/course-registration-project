import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTrigger
} from "../../../../../../components/ui/dialog.tsx";
import type {CourseSection} from "../../../../../../types/course-section.types.ts";
import {Button} from "../../../../../../components/ui/button.tsx";
import {TrashIcon} from "lucide-react";
import {useState} from "react";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {deleteCourseSection} from "../../../../../../features/coursesection/coursesection.api.ts";
import Loader from "../../../../../../components/Loader.tsx";

export default function DeleteSectionDialog({section}: { section: CourseSection }) {
  const queryClient = useQueryClient();

  const [open, setOpen] = useState(false);

  const {mutate: deleteMutation, isPending: isDeleting, error: deleteError} = useMutation({
    mutationFn: deleteCourseSection,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['course']});
    }
  });

  function handleDelete() {
    if (isDeleting) return;
    deleteMutation({sectionId: section.id});
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="destructive" className="w-fit">
          <TrashIcon/>
          Delete Section
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>Delete Course Section</DialogHeader>
        <DialogDescription>
          You are about to delete course section <b>{section.course.department}-{section.course.code}-{section.id}</b>!
          This action is irreversible. Are you sure you want to do this?
        </DialogDescription>

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
            {isDeleting ? <Loader /> : "Delete"}
          </Button>
        </DialogFooter>
      </DialogContent>

    </Dialog>
  )
}
import type {Term} from "../../../../types/term.types.ts";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger
} from "../../../../components/ui/dialog.tsx";
import {useState} from "react";
import {Button} from "../../../../components/ui/button.tsx";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {deleteTerm} from "../../../../features/term/term.api.ts";
import Loader from "../../../../components/Loader.tsx";

export default function DeleteTermDialog({term}: { term: Term }) {

  const queryClient = useQueryClient();
  const [open, setOpen] = useState(false);

  const {mutate: deleteMutation, isPending: isDeleting, error: deleteError} = useMutation({
    mutationFn: deleteTerm,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['terms']});
      setOpen(false);
    }
  })

  function handleDelete() {
    if (isDeleting) return;
    deleteMutation({termId: term.id});
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="destructive">Delete</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Delete Term</DialogTitle>
          <DialogDescription>
            You are about to delete term <b>{term.id}</b>. This action is irreversible. Are you sure you want to do
            this?
          </DialogDescription>
        </DialogHeader>

        {deleteError && (
          <p className="text-destructive text-center text-balance">{(deleteError as Error).message}</p>
        )}

        <DialogFooter className="w-full flex gap-4 items-center justify-end">
          <Button variant="outline" onClick={() => setOpen(false)} disabled={isDeleting}>
            Never mind
          </Button>
          <Button onClick={handleDelete} disabled={isDeleting} variant="destructive">
            {isDeleting ? <Loader /> : "Delete"}
          </Button>
        </DialogFooter>

      </DialogContent>
    </Dialog>
  )
}
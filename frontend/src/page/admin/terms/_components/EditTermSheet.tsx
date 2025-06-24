import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "../../../../components/ui/sheet.tsx";
import {type FormEvent, useEffect, useState} from "react";
import type {ManageTermRequest, Term} from "../../../../types/term.types.ts";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {updateTerm} from "../../../../features/term/term.api.ts";
import {Label} from "../../../../components/ui/label.tsx";
import {Input} from "../../../../components/ui/input.tsx";
import {Button} from "../../../../components/ui/button.tsx";
import Loader from "../../../../components/Loader.tsx";

export default function EditTermSheet({term}: { term: Term }) {
  const queryClient = useQueryClient();

  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<ManageTermRequest>({
    startDate: term.startDate,
    endDate: term.endDate,
    registrationStart: term.registrationStart,
    registrationEnd: term.registrationEnd
  });

  function resetFormData() {
    setFormData({
      startDate: term.startDate,
      endDate: term.endDate,
      registrationStart: term.registrationStart,
      registrationEnd: term.registrationEnd
    });
  }

  useEffect(() => {
    resetFormData();
  }, [open]);


  const {mutate: updateMutation, isPending: isUpdating, error: updateError} = useMutation({
    mutationFn: updateTerm,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['terms']});
      setOpen(false);
    }
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (isUpdating) return;
    updateMutation({termId: term.id, manageTermRequest: formData});
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button variant="ghost">Edit</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Edit Term</SheetTitle>
          <SheetDescription>Edit or modify term.</SheetDescription>
        </SheetHeader>
        <form onSubmit={handleSubmit} className="w-full flex flex-col gap-4 p-4">
          {/* Start Date */}
          <div className="flex-col-2">
            <Label htmlFor="startDate">Start Date</Label>
            <Input
              id="startDate"
              type="date"
              placeholder="Choose start date"
              value={formData.startDate}
              onChange={e => setFormData(prev => ({
                ...prev,
                startDate: e.target.value
              }))}
            />
          </div>

          {/* End Date */}
          <div className="flex-col-2">
            <Label htmlFor="endDate">End Date</Label>
            <Input
              id="endDate"
              type="date"
              placeholder="Choose end date"
              value={formData.endDate}
              onChange={e => setFormData(prev => ({
                ...prev,
                endDate: e.target.value
              }))}
            />
          </div>

          {/* Registration Start Date */}
          <div className="flex-col-2">
            <Label htmlFor="registrationStart">Registration Start Date</Label>
            <Input
              id="registrationStart"
              type="date"
              placeholder="Choose registration start date"
              value={formData.registrationStart}
              onChange={e => setFormData(prev => ({
                ...prev,
                registrationStart: e.target.value
              }))}
            />
          </div>

          {/* Registration End Date */}
          <div className="flex-col-2">
            <Label htmlFor="registrationEnd">Registration End Date</Label>
            <Input
              id="registrationEnd"
              type="date"
              placeholder="Choose registration end date"
              value={formData.registrationEnd}
              onChange={e => setFormData(prev => ({
                ...prev,
                registrationEnd: e.target.value
              }))}
            />
          </div>

          {updateError && (
            <p className="text-destructive text-center text-balance">{(updateError as Error).message}</p>
          )}
          <Button type="submit" disabled={isUpdating}>
            {isUpdating ? <Loader /> : "Save Changes"}
          </Button>
        </form>
      </SheetContent>

    </Sheet>
  )
}
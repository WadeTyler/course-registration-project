import {useMutation, useQueryClient} from "@tanstack/react-query";
import {type FormEvent, useEffect, useState} from "react";
import type {ManageTermRequest} from "../../../../types/term.types.ts";
import {createTerm} from "../../../../features/term/term.api.ts";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "../../../../components/ui/sheet.tsx";
import {Button} from "../../../../components/ui/button.tsx";
import {Label} from "../../../../components/ui/label.tsx";
import {Input} from "../../../../components/ui/input.tsx";
import Loader from "../../../../components/Loader.tsx";

export default function CreateTermSheet() {
  const queryClient = useQueryClient();

  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<ManageTermRequest>({
    startDate: "",
    endDate: "",
    registrationStart: "",
    registrationEnd: ""
  });

  function resetFormData() {
    setFormData({
      startDate: "",
      endDate: "",
      registrationStart: "",
      registrationEnd: ""
    });
  }

  useEffect(() => {
    resetFormData();
  }, [open]);


  const {mutate: createMutation, isPending: isCreating, error: createError} = useMutation({
    mutationFn: createTerm,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['terms']});
      setOpen(false);
    }
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (isCreating) return;
    createMutation(formData);
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button>Create Term</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Create Term</SheetTitle>
          <SheetDescription>Create a new Term.</SheetDescription>
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

          {createError && (
            <p className="text-destructive text-center text-balance">{(createError as Error).message}</p>
          )}
          <Button type="submit" disabled={isCreating}>
            {isCreating ? <Loader /> : "Create Term"}
          </Button>
        </form>
      </SheetContent>

    </Sheet>
  )
}
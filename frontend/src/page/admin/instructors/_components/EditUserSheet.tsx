import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "../../../../components/ui/sheet.tsx";
import {Button} from "../../../../components/ui/button.tsx";
import {type FormEvent, useEffect, useState} from "react";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {updateUserAsAdmin} from "../../../../features/auth/auth.api.ts";
import type {User} from "../../../../types/user.types.ts";
import {getUserRole} from "../../../../features/auth/auth.util.ts";
import {Label} from "../../../../components/ui/label.tsx";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "../../../../components/ui/select.tsx";
import Loader from "../../../../components/Loader.tsx";

export default function EditUserSheet({user}: {user: User}) {
  const queryClient = useQueryClient();

  const userRole = getUserRole(user);

  const [open, setOpen] = useState(false);
  const [role, setRole] = useState<"ADMIN" | "INSTRUCTOR" | "STUDENT">(userRole);


  function resetRole() {
    setRole(userRole);
  }

  useEffect(resetRole, [open]);

  const {mutate: updateMutation, isPending: isUpdating, error: updateError} = useMutation({
    mutationFn: updateUserAsAdmin,
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ['users']});
      setOpen(false);
    }
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();
    if (isUpdating) return;
    updateMutation({userId: user.id, role});

  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button>Edit</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>
            Edit User
          </SheetTitle>
          <SheetDescription>
            Update user details and role.
          </SheetDescription>
        </SheetHeader>
        <form onSubmit={handleSubmit} className="w-full flex flex-col gap-4 p-4">
          <div className="flex-col-2">
            <Label htmlFor="role">Role</Label>
            <Select value={role} onValueChange={e => setRole(e as "ADMIN" | "INSTRUCTOR" | "STUDENT")}>
              <SelectTrigger className="w-full">
                <SelectValue id="role" placeholder={"Choose user role"} />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectItem value={"ADMIN"}>ADMIN</SelectItem>
                  <SelectItem value={"INSTRUCTOR"}>INSTRUCTOR</SelectItem>
                  <SelectItem value={"STUDENT"}>STUDENT</SelectItem>
                </SelectGroup>
              </SelectContent>

            </Select>
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
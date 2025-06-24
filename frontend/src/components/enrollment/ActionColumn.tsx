import type {Enrollment} from "../../types/enrollment.types.ts";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger
} from "../ui/dropdown-menu.tsx";
import {MoreHorizontal} from "lucide-react";
import DropEnrollmentDialog from "./DropEnrollmentDialog.tsx";
import {useQuery} from "@tanstack/react-query";
import {getAuthUser} from "../../features/auth/auth.api.ts";
import {isAdmin, isInstructor} from "../../features/auth/auth.util.ts";
import EditEnrollmentSheet from "./EditEnrollmentSheet.tsx";

export function ActionColumn({enrollment}: { enrollment: Enrollment }) {

  const {data: authUser} = useQuery({
    queryKey: ['authUser'],
    queryFn: getAuthUser
  });

  return (
    <DropdownMenu>
      <DropdownMenuTrigger>
        <MoreHorizontal/>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="flex-col-2">
        <DropdownMenuLabel>Actions</DropdownMenuLabel>
        {authUser && (isAdmin(authUser) || isInstructor(authUser)) && (
          <DropdownMenuItem asChild>
            <EditEnrollmentSheet enrollment={enrollment}/>
          </DropdownMenuItem>
        )}
        <DropdownMenuItem asChild>
          <DropEnrollmentDialog enrollment={enrollment}/>
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
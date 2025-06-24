"use client"

import type {ColumnDef} from "@tanstack/react-table"
import {ArrowUpDown, MoreHorizontal} from "lucide-react";
import type {InstructorEnrollment} from "../../../../../types/enrollment.types.ts";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger
} from "../../../../../components/ui/dropdown-menu.tsx";
import {Button} from "../../../../../components/ui/button.tsx";
import DropInstructorEnrollmentDialog from "./DropInstructorEnrollmentDialog.tsx";
import EditInstructorEnrollmentSheet from "./EditInstructorEnrollmentSheet.tsx";

// This type is used to define the shape of our data.
// You can use a Zod schema here if you want.

export const instructorEnrollmentColumns: ColumnDef<InstructorEnrollment>[] = [
  {
    accessorKey: "actions",
    header: "Actions",
    cell: ({row}) => {

      const enrollment = row.original;
      return (
        <DropdownMenu>
          <DropdownMenuTrigger>
            <MoreHorizontal/>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="flex-col-2">
            <DropdownMenuLabel>Actions</DropdownMenuLabel>
            <DropdownMenuItem asChild>
              <EditInstructorEnrollmentSheet enrollment={enrollment} />
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <DropInstructorEnrollmentDialog enrollment={enrollment} />
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      )
    }
  },
  {
    id: "firstName",
    accessorFn: row => row.student.firstName,
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          First Name
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    id: "lastName",
    accessorFn: row => row.student.lastName,
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Last Name
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "grade",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Grade
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "status",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Status
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
];


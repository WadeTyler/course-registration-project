"use client"

import type {ColumnDef} from "@tanstack/react-table"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger
} from "../../../../components/ui/dropdown-menu.tsx";
import {ArrowUpDown, MoreHorizontal} from "lucide-react";
import type {Term} from "../../../../types/term.types.ts";
import EditTermSheet from "./EditTermSheet.tsx";
import DeleteTermDialog from "./DeleteTermDialog.tsx";
import {Button} from "../../../../components/ui/button.tsx";

// This type is used to define the shape of our data.
// You can use a Zod schema here if you want.


export const columns: ColumnDef<Term>[] = [
  {
    accessorKey: "actions",
    header: "Actions",
    cell: ({row}) => {

      const term = row.original;
      return (
        <DropdownMenu>
          <DropdownMenuTrigger>
            <MoreHorizontal/>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="flex-col-2">
            <DropdownMenuLabel>Actions</DropdownMenuLabel>
            <DropdownMenuItem asChild><EditTermSheet term={term}/></DropdownMenuItem>
            <DropdownMenuItem asChild><DeleteTermDialog term={term}/></DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      )
    }
  },
  {
    accessorKey: "id",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          ID
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
    cell: ({ row }) => {
      return row.original.id.toString();
    }
  },
  {
    accessorKey: "startDate",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Start Date
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "endDate",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          End Date
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "registrationStart",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Registration Start
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "registrationEnd",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Registration End
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
]
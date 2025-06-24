"use client"

import type {ColumnDef} from "@tanstack/react-table"
import type {Course} from "../../../../types/course.types.ts";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger
} from "../../../../components/ui/dropdown-menu.tsx";
import {ArrowUpDown, MoreHorizontal} from "lucide-react";
import {Link} from "react-router-dom";
import {Button} from "../../../../components/ui/button.tsx";

// This type is used to define the shape of our data.
// You can use a Zod schema here if you want.


export const columns: ColumnDef<Course>[] = [
  {
    accessorKey: "actions",
    header: "Actions",
    cell: ({row}) => {

      const course = row.original;
      return (
        <DropdownMenu>
          <DropdownMenuTrigger>
            <MoreHorizontal/>
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuLabel>Actions</DropdownMenuLabel>
            <Link to={`/admin/courses/${course.id}`}>
              <DropdownMenuItem>Manage</DropdownMenuItem>
            </Link>
          </DropdownMenuContent>
        </DropdownMenu>
      )
    }
  },
  {
    accessorKey: "department",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Department
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "code",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Code
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "title",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Title
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "description",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Description
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "credits",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Credits
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "sectionsLength",
    header: "Sections",
    cell: ({row}) => {
      return row.original.courseSections.length;
    }
  },
  {
    accessorKey: "prerequisitesLength",
    header: "Prerequisites",
    cell: ({row}) => {
      return row.original.prerequisites.length;
    }
  }
]
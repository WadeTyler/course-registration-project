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
import {MoreHorizontal} from "lucide-react";
import {Link} from "react-router-dom";

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
    header: "Department",
  },
  {
    accessorKey: "code",
    header: "Code",
  },
  {
    accessorKey: "title",
    header: "Title",
  },
  {
    accessorKey: "description",
    header: "Description",
  },
  {
    accessorKey: "credits",
    header: "Credits",
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
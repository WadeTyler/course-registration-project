"use client"

import type {ColumnDef} from "@tanstack/react-table"
import {ArrowUpDown} from "lucide-react";
import type {Enrollment} from "../../types/enrollment.types.ts";
import {Button} from "../ui/button.tsx";
import {ActionColumn} from "./ActionColumn.tsx";

// This type is used to define the shape of our data.
// You can use a Zod schema here if you want.

export const enrollmentColumns: ColumnDef<Enrollment>[] = [
  {
    accessorKey: "actions",
    header: "Actions",
    cell: ({row}) => {
      const enrollment = row.original;
      return (
        <ActionColumn enrollment={enrollment} />
      )
    }
  },
  {
    accessorKey: "section",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Course Section
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
    cell: ({row}) => {
      const section = row.original.courseSection;
      return (section.course.department + "-" + section.course.code + "-" + section.id) as string;
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
  {
    id: "startDate",
    accessorFn: (row) => row.courseSection.term.startDate,
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Starts
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
    cell: ({row}) => {
      return row.original.courseSection.term.startDate;
    },
  },
  {
    id: "endDate",
    accessorFn: (row) => row.courseSection.term.endDate,
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Ends
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
    cell: ({row}) => {
      return row.original.courseSection.term.endDate;
    }
  },
];


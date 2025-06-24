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
import {Button} from "../../../../components/ui/button.tsx";
import type {User} from "../../../../types/user.types.ts";
import {getUserRole} from "../../../../features/auth/auth.util.ts";
import {Link} from "react-router-dom";

// This type is used to define the shape of our data.
// You can use a Zod schema here if you want.


export const columns: ColumnDef<User>[] = [
  {
    accessorKey: "actions",
    header: "Actions",
    cell: ({row}) => {

      const user = row.original;

      return (
        <DropdownMenu>
          <DropdownMenuTrigger>
            <MoreHorizontal/>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="flex-col-2">
            <DropdownMenuLabel>Actions</DropdownMenuLabel>
            <Link to={`/admin/students/${user.id}`}>
              <DropdownMenuItem>
                View
              </DropdownMenuItem>
            </Link>
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
    cell: ({row}) => {
      return row.original.id.toString();
    }
  },
  {
    accessorKey: "username",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Email
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
  },
  {
    accessorKey: "firstName",
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
    accessorKey: "lastName",
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
    accessorKey: "role",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Role
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
    cell: ({row}) => {
      const user = row.original;
      return getUserRole(user);
    }
  },
  {
    accessorKey: "createdAt",
    header: ({column}) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          User Since
          <ArrowUpDown className="ml-2 h-4 w-4"/>
        </Button>
      );
    },
    cell: ({row}) => {
      const user = row.original;
      return new Date(user.createdAt).toLocaleDateString();
    }
  }
]
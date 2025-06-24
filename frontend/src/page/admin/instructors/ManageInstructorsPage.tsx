import {useQuery} from "@tanstack/react-query";
import {getAllUsers} from "../../../features/auth/auth.api.ts";
import Loader from "../../../components/Loader.tsx";
import {DataTable} from "../../../components/data-table.tsx";
import {columns} from "./_components/columns.tsx";
import {isAdmin, isInstructor} from "../../../features/auth/auth.util.ts";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator
} from "../../../components/ui/breadcrumb.tsx";

export default function ManageInstructorsPage() {
  
  const {data: users, isPending: isLoadingUsers, error: loadUsersError} = useQuery({
    queryKey: ['users'],
    queryFn: getAllUsers
  });
  
  return (
    <div className="p-page">
      <div className="container mx-auto flex flex-col gap-8">
        <Breadcrumb>
          <BreadcrumbList>
            <BreadcrumbItem>
              <BreadcrumbLink href="/admin">Admin Dashboard</BreadcrumbLink>
            </BreadcrumbItem>
            <BreadcrumbSeparator/>
            <BreadcrumbItem>
              <BreadcrumbPage>Manage Instructors</BreadcrumbPage>
            </BreadcrumbItem>
          </BreadcrumbList>
        </Breadcrumb>

        <div>
          <h1 className="title">Manage Instructors</h1>
          <p>View and manage your instructors.</p>
        </div>

        {isLoadingUsers && <Loader />}
        {loadUsersError && (
          <p className="text-center text-destructive text-balance">{(loadUsersError as Error).message}</p>
        )}
        {users && !loadUsersError && (
          <DataTable columns={columns} data={users.filter(u => isInstructor(u) || isAdmin(u))} />
        )}
        
        
      </div>
    </div>
  )
}
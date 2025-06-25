import {useQuery} from "@tanstack/react-query";
import {getAllAssignedCourseSections} from "@/features/coursesection/coursesection.api.ts";
import Loader from "../../../components/Loader.tsx";
import {DataTable} from "@/components/data-table.tsx";
import {assignedSectionColumns} from "./_components/assigned-sections-columns.tsx";
import {
  Breadcrumb,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {useEffect} from "react";

export default function ManageInstructorSections() {
  useEffect(() => {
    document.title = "Assigned Sections | Register R Us";
  }, []);

  const {data: assignedSections, isPending: isLoadingSections, error: loadSectionsError} = useQuery({
    queryKey: ['assignedSections'],
    queryFn: getAllAssignedCourseSections
  });

  return (
    <div className="p-page">
      <div className="container mx-auto flex flex-col gap-8">

        <Breadcrumb>
          <BreadcrumbList>
            <BreadcrumbLink href="/instructor">Instructor Dashboard</BreadcrumbLink>
            <BreadcrumbSeparator />
            <BreadcrumbPage>Manage Sections</BreadcrumbPage>
          </BreadcrumbList>
        </Breadcrumb>

        <div>
          <h1 className="title">Manage Sections</h1>
          <p>View and manage your course sections.</p>
        </div>

        {isLoadingSections && <Loader />}
        {loadSectionsError && (
          <p className="text-center text-destructive text-balance">{(loadSectionsError as Error).message}</p>
        )}
        {assignedSections && (
          <DataTable columns={assignedSectionColumns} data={assignedSections} />
        )}
      </div>
    </div>
  )
}
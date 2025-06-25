import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {useQuery} from "@tanstack/react-query";
import {getAllTerms} from "@/features/term/term.api.ts";
import Loader from "../../../components/Loader.tsx";
import {DataTable} from "@/components/data-table.tsx";
import {columns} from "./_components/columns.tsx";
import CreateTermSheet from "./_components/CreateTermSheet.tsx";
import {useEffect} from "react";

export default function ManageTermsPage() {
  useEffect(() => {
    document.title = "Manage Terms | Register R Us";
  }, []);

  const {data: terms, isPending: isLoadingTerms, error: loadTermsError} = useQuery({
    queryKey: ['terms'],
    queryFn: getAllTerms
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
              <BreadcrumbPage>Manage Terms</BreadcrumbPage>
            </BreadcrumbItem>
          </BreadcrumbList>
        </Breadcrumb>

        <div>
          <h1 className="title">Manage Terms</h1>
          <p>View and manage your terms.</p>
        </div>

        <div className="ml-auto flex items-center gap-4">
          <CreateTermSheet />
        </div>

        {isLoadingTerms && <Loader/>}
        {loadTermsError && <p className="text-destructive text-center text-balance">
          {(loadTermsError as Error).message}
        </p>}
        {!loadTermsError && terms && (
          <DataTable columns={columns} data={terms} />
        )}

      </div>
    </div>
  )
}
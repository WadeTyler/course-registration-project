import {useQuery} from "@tanstack/react-query";
import {getAllCourses} from "@/features/course/course.api.ts";
import Loader from "../../../components/Loader.tsx";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {useState} from "react";
import {Label} from "@/components/ui/label.tsx";
import {Accordion} from "@/components/ui/accordion.tsx";
import CourseAccordionItem from "./_components/CourseAccordionItem.tsx";
import {
  Breadcrumb,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";


export default function CoursesCatalogPage() {


  const {data: courses, isPending: isLoadingCourses, error: loadCoursesError} = useQuery({
    queryKey: ['courses'],
    queryFn: getAllCourses
  });


  const [department, setDepartment] = useState<string | undefined>(undefined);

  const uniqueDepartments = Array.from(
    new Set(courses?.map(course => course.department))
  ).sort();

  return (
    <div className="p-page">
      <div className="container mx-auto flex flex-col gap-8">
        <Breadcrumb>
          <BreadcrumbList>
            <BreadcrumbLink href="/student">Student Dashboard</BreadcrumbLink>
            <BreadcrumbSeparator />
            <BreadcrumbPage>Course Catalog</BreadcrumbPage>
          </BreadcrumbList>
        </Breadcrumb>

        <div>
          <h1 className="title">Course Catalog</h1>
          <p>Browse courses and register for upcoming sections.</p>
        </div>

        {isLoadingCourses && <Loader/>}
        {loadCoursesError && (
          <p className="text-center text-destructive text-balance">{(loadCoursesError as Error).message}</p>
        )}
        {!loadCoursesError && courses && (
          <Card>
            <CardContent>
              <div className="flex-col-2">
                <Label>Department</Label>
                <Select onValueChange={e => setDepartment(e)} value={department}>
                  <SelectTrigger>
                    <SelectValue placeholder="Select a department to get started"></SelectValue>
                  </SelectTrigger>
                  <SelectContent>
                    {uniqueDepartments.map(department => (
                      <SelectItem key={department} value={department}>{department}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              {department && (
                <Accordion type="multiple">
                  {courses.filter(course => course.department === department).map(course => (
                    <CourseAccordionItem course={course} key={course.id}/>
                  ))}
                </Accordion>
              )}
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  )
}




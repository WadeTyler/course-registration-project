import type {Course} from "../../../../../../types/course.types.ts";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger
} from "../../../../../../components/ui/sheet.tsx";
import {type FormEvent, useEffect, useState} from "react";
import {Button} from "../../../../../../components/ui/button.tsx";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {createCourseSection} from "../../../../../../features/coursesection/coursesection.api.ts";
import type {ManageCourseSectionRequest} from "../../../../../../types/course-section.types.ts";
import {getAllTerms} from "../../../../../../features/term/term.api.ts";
import {getAllUsers} from "../../../../../../features/auth/auth.api.ts";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue
} from "../../../../../../components/ui/select.tsx";
import {Label} from "../../../../../../components/ui/label.tsx";
import {isAdmin, isInstructor} from "../../../../../../features/auth/auth.util.ts";
import {Input} from "../../../../../../components/ui/input.tsx";
import Loader from "../../../../../../components/Loader.tsx";

export default function CreateCourseSectionSheet({course}: { course: Course }) {
  const queryClient = useQueryClient();

  const {data: terms} = useQuery({
    queryKey: ['terms'],
    queryFn: getAllTerms
  });

  const {data: users} = useQuery({
    queryKey: ['users'],
    queryFn: getAllUsers
  });

  // States
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<ManageCourseSectionRequest>({
    termId: 0,
    instructorId: 0,
    room: '',
    capacity: 0,
    schedule: ''
  });

  function resetFormData() {
    setFormData({
      termId: 0,
      instructorId: 0,
      room: '',
      capacity: 0,
      schedule: ''
    });
  }

  useEffect(() => {
    resetFormData();
  }, [open]);

  const {mutate: createSectionMutation, isPending: isCreating, error: createError} = useMutation({
    mutationFn: createCourseSection,
    onSuccess: () => {
      queryClient.invalidateQueries({queryKey: ['course']});
      setOpen(false);
    }
  });

  function handleSubmit(e: FormEvent) {
    e.preventDefault();

    if (isCreating) return;

    createSectionMutation({courseId: course.id, manageCourseSectionRequest: formData});
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button>Create Section</Button>
      </SheetTrigger>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Create Section</SheetTitle>
          <SheetDescription>Create a new course section for {course.title}.</SheetDescription>
        </SheetHeader>
        <form className="w-full p-4 flex flex-col gap-4" onSubmit={handleSubmit}>
          {/* Term */}
          <div className="flex-col-2">
            <Label htmlFor="term">Term</Label>
            <Select
              value={formData.termId === 0 ? "" : formData.termId.toString()}
              onValueChange={e => setFormData(prev => ({...prev, termId: parseInt(e)}))}
            >
              <SelectTrigger className="w-full">
                <SelectValue placeholder="Choose a Term" id="term"/>
              </SelectTrigger>
              <SelectContent>
                {terms?.map(term => (
                  <SelectItem value={term.id.toString()}>{term.startDate} to {term.endDate}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {/* Instructor */}
          <div className="flex-col-2">
            <Label htmlFor="instructor">Instructor</Label>
            <Select
              value={formData.instructorId === 0 ? "" : formData.instructorId.toString()}
              onValueChange={e => setFormData(prev => ({
                ...prev,
                instructorId: e ? parseInt(e) : 0
              }))}
            >
              <SelectTrigger className="w-full">
                <SelectValue placeholder="Choose an Instructor" id="instructor"/>
              </SelectTrigger>
              <SelectContent>
                {users?.filter(user => isInstructor(user) || isAdmin(user))
                  .map(instructor => (
                    <SelectItem
                      value={instructor.id.toString()}>{instructor.firstName} {instructor.lastName}</SelectItem>
                  ))}
              </SelectContent>
            </Select>
          </div>

          {/* Capacity */}
          <div className="flex-col-2">
            <Label htmlFor="capacity">Capacity</Label>
            <Input
              id="capacity"
              type="number"
              placeholder="Enter a capacity"
              value={formData.capacity}
              onChange={e => setFormData(prev => ({
                ...prev,
                capacity: e.target.valueAsNumber
              }))}
            />
          </div>

          {/* Room */}
          <div className="flex-col-2">
            <Label htmlFor="room">Room</Label>
            <Input
              id="room"
              placeholder="Enter Room"
              value={formData.room}
              onChange={e => setFormData(prev => ({
                ...prev,
                room: e.target.value
              }))}
            />
          </div>

          {/* Schedule */}
          <div className="flex-col-2">
            <Label htmlFor="schedule">Schedule</Label>
            <Input
              id="schedule"
              placeholder="Enter Schedule"
              value={formData.schedule}
              onChange={e => setFormData(prev => ({
                ...prev,
                schedule: e.target.value
              }))}
            />
          </div>

          {createError && (
            <p className="text-destructive text-center text-balance">
              {(createError as Error).message}
            </p>
          )}

          <Button type="submit">
            {isCreating ? <Loader /> : "Create Section"}
          </Button>
        </form>
      </SheetContent>

    </Sheet>
  )
}

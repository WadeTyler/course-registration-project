import {Link} from "react-router-dom";
import {Avatar, AvatarFallback} from "./ui/avatar.tsx";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {getAuthUser, logout} from "../features/auth/auth.api.ts";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger
} from "./ui/dropdown-menu.tsx";
import {isAdmin, isInstructor, isStudent} from "../features/auth/auth.util.ts";

export default function Navbar() {

  const queryClient = useQueryClient();
  const {data: authUser} = useQuery({
    queryKey: ['authUser'],
    queryFn: getAuthUser
  });

  const {mutate: handleLogout, isPending: isLoggingOut} = useMutation({
    mutationFn: logout,
    onSuccess: async () => {
      queryClient.invalidateQueries({queryKey: ['authUser']});
    }
  });

  return (
    <header className="w-full h-16 fixed top-0 bg-accent/90 backdrop-blur-sm shadow-md flex items-center justify-center z-50 p-4">
      <nav className="container mx-auto flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Link to={"/"}>
            <img
              src="/rrs-logo-white.png"
              alt="Home"
              className="w-32"
            />
          </Link>

          {/* Page Options */}
          {authUser && (
            <div className="hidden md:flex items-center gap-4 text-accent-foreground mx-auto text-sm">
              {isAdmin(authUser) && (
                <>
                  <Link to="/admin/courses">Courses</Link>
                  <Link to="/admin/terms">Terms</Link>
                  <Link to="/admin/instructors">Instructors</Link>
                  <Link to="/admin/students">Students</Link>
                  <Link to={"/instructor"}>Instructor Dashboard</Link>
                </>
              )}
              {isInstructor(authUser) && (
                <>
                  <Link to={"/instructor/sections"}>Sections</Link>
                </>
              )}
              {isStudent(authUser) && (
                <>
                  <Link to="/student/courses">Course Catalog</Link>
                </>
              )}
            </div>
          )}
        </div>


        {/* Account Options */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Avatar className="shadow-md">
              <AvatarFallback>
                {authUser?.firstName[0].toUpperCase()}{authUser?.lastName[0].toUpperCase()}
              </AvatarFallback>
            </Avatar>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuLabel>Account</DropdownMenuLabel>
            <DropdownMenuItem disabled={isLoggingOut} onClick={() => handleLogout()}>
              Logout
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>

      </nav>
    </header>
  )
}
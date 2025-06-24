import {Link} from "react-router-dom";

export default function Navbar() {
  return (
    <header className="w-full h-16 fixed top-0 bg-blue-900 flex items-center justify-center z-50">
      <nav className="container mx-auto flex items-center justify-between">
        <Link to={"/"}>
          <img
            src="/src/assets/rrs-logo-white.png"
            alt="Home"
            className="w-32"
          />
        </Link>
      </nav>
    </header>
  )
}
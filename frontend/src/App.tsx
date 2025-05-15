import './styles/App.css'
import HomePage from "./page/HomePage.tsx";
import {Route, Routes} from "react-router-dom";

function App() {

  return (
    <Routes>
      {/* Routes go here. Path is the endpoint for the element. */}
      <Route path="/" element={<HomePage />} />
    </Routes>
  )
}

export default App

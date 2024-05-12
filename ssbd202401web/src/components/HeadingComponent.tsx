import { Link } from "react-router-dom";

export default function HeadingComponent() {
  return (
    <Link
      style={{
        textDecoration: "none",
        color: "inherit",
      }}
      to={"/"}
    >
      EventSymphony
    </Link>
  );
}

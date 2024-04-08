import { Link } from "react-router-dom";
import LinkPropType from "../types/LinkPropType";

export default function LinkComponent(props: LinkPropType) {
    return (
        <Link style={{
            textDecoration: "none",
            color: "inherit",
            fontSize: "18px",
            fontWeight: 500
        }} to={props.href}>{props.name}</Link>
    )
}
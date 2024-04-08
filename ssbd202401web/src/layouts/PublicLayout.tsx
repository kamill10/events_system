import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";

export default function PublicLayout(props: PublicLayoutPropType) {
    return (
        <div id={"public"}>
            {props.Page}
        </div>
    )
}
import {Route, Routes} from "react-router-dom";
import {PublicRoutes} from "./Routes.ts";
import PublicLayout from "../layouts/PublicLayout.tsx";

export default function RouterComponent() {
    return (
        <Routes>
            {PublicRoutes.map((route, key) => {
                return <Route key={key} path={route.pathname}
                              element={<PublicLayout page={route.page}></PublicLayout>}></Route>
            })}
        </Routes>
    )
}
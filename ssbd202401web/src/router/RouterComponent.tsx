import {Route, Routes} from "react-router-dom";
import {PublicRoutes} from "./Routes.ts";
import PublicLayout from "../layouts/PublicLayout.tsx";

export default function RouterComponent() {
    return (
        <Routes>
            {PublicRoutes.map((route, key) => {
                return <Route key={key} path={route.Pathname} element={<PublicLayout Page={route.Page}></PublicLayout>}></Route>
            })}
        </Routes>
    )
}
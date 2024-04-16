import {Route, Routes} from "react-router-dom";
import {AdminRoutes, ManagerRoutes, ParticipantRoutes, PublicRoutes} from "./Routes.ts";
import PublicLayout from "../layouts/PublicLayout.tsx";
import ManagerLayout from "../layouts/ManagerLayout.tsx";
import ParticipantLayout from "../layouts/ParticipantLayout.tsx";
import AdminLayout from "../layouts/AdminLayout.tsx";

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
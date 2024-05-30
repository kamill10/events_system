import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";
import { Pathnames } from "../router/Pathnames";

export default function RedirectPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, isAdmin, isManager, isParticipant } = useAccount();

  useEffect(() => {
    if (
      !isAuthenticated &&
      !Object.values(Pathnames.public).includes(location.pathname)
    ) {
      navigate(Pathnames.public.home);
    } else if (
      isParticipant &&
      !Object.values(Pathnames.participant).includes(location.pathname)
    ) {
      navigate(Pathnames.participant.home);
    } else if (
      isManager &&
      !Object.values(Pathnames.manager).includes(location.pathname)
    ) {
      navigate(Pathnames.manager.home);
    } else if (
      isAdmin &&
      !Object.values(Pathnames.admin).includes(location.pathname)
    ) {
      navigate(Pathnames.admin.home);
    } else {
      navigate(location.pathname);
    }
  }, [isAuthenticated, isAdmin, isManager, isParticipant]);

  return <></>;
}

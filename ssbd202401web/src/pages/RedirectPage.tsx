import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";
import { Pathnames } from "../router/Pathnames";

export default function RedirectPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, isAdmin, isManager, isParticipant } = useAccount();

  function determineRoutes() {
    let result = Object.values(Pathnames.public).concat(
      Object.values(Pathnames.singlePage),
    );
    if (!isAuthenticated) {
      return result.concat(Object.values(Pathnames.unauth));
    }
    if (isAuthenticated) {
      result = result.concat(Object.values(Pathnames.auth));
    }
    if (isParticipant) {
      result = result.concat(Object.values(Pathnames.participant));
    }
    if (isManager) {
      result = result.concat(Object.values(Pathnames.manager));
    }
    if (isAdmin) {
      result = result.concat(Object.values(Pathnames.admin));
    }
    return result;
  }

  useEffect(() => {
    const paths = determineRoutes();
    if (paths.includes(location.pathname)) {
      navigate(location.pathname);
    } else {
      navigate(Pathnames.public.error);
    }
  }, [isAuthenticated, isAdmin, isManager, isParticipant]);

  return <></>;
}

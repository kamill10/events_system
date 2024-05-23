import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";

export default function Logout() {
  const { logOut } = useAccount();

  useEffect(() => {
    logOut();
  }, []);

  return <></>;
}

import { Typography } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";

export default function HomePage() {
  const { getMyAccount, isAuthenticated } = useAccount();

  useEffect(() => {
    if (isAuthenticated) getMyAccount();
  }, []);

  return <Typography variant={"h4"}>Home page</Typography>;
}

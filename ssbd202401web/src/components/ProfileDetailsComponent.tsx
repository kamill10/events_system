import {
  Box,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";

export default function ProfileDetailsComponent() {
  const { account, getMyAccount } = useAccount();

  useEffect(() => {
    getMyAccount();
  }, []);

  const data = [
    { ID: account?.id },
    { Username: account?.username },
    { Roles: account?.roles },
    { "E-mail": account?.email },
    { "First name": account?.firstName },
    { "Last name": account?.lastName },
    { Gender: account?.gender },
    { "Is active": account?.active ? "Yes" : "No" },
    { "Is verified": account?.verified ? "Yes" : "No" },
    { "Is unlocked": account?.nonLocked ? "Yes" : "No" },
    { "Language preference": account?.language },
  ];
  return (
    <Box
      sx={{
        marginTop: 4,
        marginLeft: 5,
      }}
    >
      <Typography variant="h4">Change personal data</Typography>
      <TableContainer>
        <TableHead>
          <TableCell
            sx={{
              fontWeight: "bold",
              fontSize: "18px",
            }}
          >
            Key
          </TableCell>
          <TableCell
            sx={{
              fontWeight: "bold",
              fontSize: "18px",
            }}
          >
            Value
          </TableCell>
        </TableHead>
        <TableBody>
          {data.map((_, value) => {
            return (
              <TableRow hover>
                <TableCell>{Object.keys(data[value])}</TableCell>
                <TableCell>{Object.values(data[value])}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </TableContainer>
    </Box>
  );
}

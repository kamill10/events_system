import {
  Button,
  Divider,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { useEffect } from "react";
import { useManageAccounts } from "../hooks/useManageAccounts";
import AccountRowComponent from "../components/AccountRowComponent";
import ContainerComponent from "../components/ContainerComponent";

export default function AccountsPage() {
  const { accounts, getAllAccounts } = useManageAccounts();

  useEffect(() => {
    getAllAccounts();
  }, []);

  return (
    <ContainerComponent>
      <Typography variant="h3">Manage accounts</Typography>
      <Divider
        sx={{
          marginTop: "1rem",
          marginBottom: "2rem",
        }}
      ></Divider>
      <Button
        variant="contained"
        onClick={getAllAccounts}
        sx={{
          margin: "1rem",
        }}
      >
        Refresh data
      </Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow hover>
              <TableCell
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                ID
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                Username
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                First name
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                Last name
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                E-mail
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {accounts?.map((account) => {
              return (
                <AccountRowComponent account={account}></AccountRowComponent>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </ContainerComponent>
  );
}

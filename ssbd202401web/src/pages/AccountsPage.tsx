import {
  Breadcrumbs,
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
import { Link } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import RefreshIcon from "@mui/icons-material/Refresh";

export default function AccountsPage() {
  const { accounts, getAllAccounts } = useManageAccounts();

  useEffect(() => {
    getAllAccounts();
  }, []);

  return (
    <ContainerComponent>
      <Breadcrumbs aria-label="breadcrumb" sx={{
        marginBottom: 3
      }}>
        <Link to={Pathnames.public.home} style={{
          textDecoration: "none",
          color: "black"
        }}>
          Home
        </Link>
        <Link to={Pathnames.admin.accounts} style={{
          textDecoration: "none",
          color: "black",
          fontWeight: "bold"
        }}>
          Accounts
        </Link>
        <Typography color={"grey"}>Account details</Typography>
      </Breadcrumbs>
      <Typography variant="h3">Manage accounts</Typography>
      <Divider
        sx={{
          marginTop: "1rem",
          marginBottom: "2rem",
        }}
      ></Divider>
      <Button
        variant="contained"
        startIcon={<RefreshIcon></RefreshIcon>}
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
                E-mail
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                Roles
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                Is active
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {accounts?.map(account => {
              return <AccountRowComponent key={account.id} account={account}></AccountRowComponent>
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </ContainerComponent>
  );
}

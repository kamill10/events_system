import {
  Breadcrumbs,
  Button,
  Divider,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TextField,
  TableHead,
  TableRow,
  Typography,
  MenuItem,
  ToggleButton,
  ToggleButtonGroup,
  TablePagination,
  Accordion,
  AccordionSummary,
  AccordionDetails,
} from "@mui/material";
import { useEffect, useState } from "react";
import RefreshIcon from "@mui/icons-material/Refresh";
import { useManageAccounts } from "../hooks/useManageAccounts";
import AccountRowComponent from "../components/AccountRowComponent";
import ContainerComponent from "../components/ContainerComponent";
import { Link } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";
import { useTranslation } from "react-i18next";
import { SortingRequestParams } from "../types/SortingRequestParams.ts";
import { yupResolver } from "@hookform/resolvers/yup";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import FormComponent from "../components/FormComponent.tsx";
import TextFieldComponent from "../components/TextFieldComponent.tsx";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import { SortingRequestParamsSchema } from "../validation/schemas.ts";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";

export default function AccountsPage() {
  const { t } = useTranslation();
  const { accounts, getAccountsWithPagination } = useManageAccounts();
  const { account } = useAccount();
  const [open, setOpen] = useState(false);
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
    getValues,
  } = useForm<SortingRequestParams>({
    defaultValues: {
      page: 0,
      size: 5,
      direction: "asc",
      key: "id",
      phrase: "",
    },
    resolver: yupResolver(SortingRequestParamsSchema),
  });

  const onSubmit: SubmitHandler<SortingRequestParams> = async (data) => {
    await getAccountsWithPagination(data);
    setOpen(false);
  };

  useEffect(() => {
    getAccountsWithPagination(getValues());
  }, []);

  const onError: SubmitErrorHandler<SortingRequestParams> = (error) => {
    console.error(error);
  };

  const handleChangePage = (_: any, newPage: number) => {
    setValue("page", newPage);
    handleSubmit(onSubmit)();
  };

  const handleChangeRowsPerPage = (event: any) => {
    const newRowsPerPage = parseInt(event.target.value, 10);
    setValue("page", 0);
    setValue("size", newRowsPerPage);
    handleSubmit(onSubmit)();
  };

  return (
    <ContainerComponent>
      <Breadcrumbs aria-label="breadcrumb" sx={{ marginBottom: 3 }}>
        <Link to="/home" style={{ textDecoration: "none", color: "black" }}>
          {t("home")}
        </Link>
        <Link
          to="/admin/accounts"
          style={{
            textDecoration: "none",
            color: "black",
            fontWeight: "bold",
          }}
        >
          {t("accounts")}
        </Link>
        <Typography color="grey">{t("accountDetails")}</Typography>
      </Breadcrumbs>
      <Typography variant="h3">{t("manageAccounts")}</Typography>
      <Divider sx={{ marginTop: "1rem", marginBottom: "2rem" }} />
      <Button
        variant="contained"
        sx={{
          marginY: 2,
        }}
        startIcon={<RefreshIcon />}
        onClick={() => getAccountsWithPagination(getValues())}
      >
        {t("refreshData")}
      </Button>
      <Accordion
        expanded={open}
        onChange={() => setOpen(!open)}
        sx={{
          marginBottom: "1rem",
        }}
      >
        <AccordionSummary expandIcon={<ArrowDownwardIcon />}>
          <Typography variant="h5">{t("filterData")}</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <FormComponent
            handleSubmit={handleSubmit}
            onError={onError}
            onSubmit={onSubmit}
            align="start"
          >
            <Typography variant="h4">{t("filterByPhraseTitle")}</Typography>
            <Typography variant="body1">{t("filterByPhraseBody")}</Typography>
            <TextFieldComponent
              control={control}
              errors={errors}
              label={t("searchPhrase")}
              name="phrase"
              trigger={trigger}
              type="text"
            ></TextFieldComponent>
            <Typography variant="h4">{t("sortDataKeyTitle")}</Typography>
            <Typography variant="body1">{t("sortDataKeyBody")}</Typography>
            <Controller
              name="key"
              control={control}
              render={({ field }) => {
                return (
                  <TextField
                    select
                    label={t("filterKey")}
                    fullWidth
                    value={field.value}
                    onChange={(e) => {
                      field.onChange(e);
                    }}
                    id={field.name}
                    sx={{ marginTop: "1rem" }}
                    name={field.name}
                    autoComplete=""
                  >
                    <MenuItem value="id">{t("id")}</MenuItem>
                    <MenuItem value="username">{t("userName")}</MenuItem>
                    <MenuItem value="firstName">{t("firstName")}</MenuItem>
                    <MenuItem value="lastName">{t("lastName")}</MenuItem>
                    <MenuItem value="email">{t("e-mail")}</MenuItem>
                    <MenuItem value="roles">{t("roles")}</MenuItem>
                    <MenuItem value="active">{t("isActive")}</MenuItem>
                  </TextField>
                );
              }}
            ></Controller>
            <Typography variant="h4">{t("sortDataDirTitle")}</Typography>
            <Typography variant="body1">{t("sortDataDirBody")}</Typography>
            <Controller
              name="direction"
              control={control}
              render={({ field }) => {
                return (
                  <ToggleButtonGroup
                    value={field.value}
                    exclusive
                    sx={{
                      marginTop: "1rem",
                    }}
                    onChange={(e) => {
                      field.onChange(e);
                    }}
                    id={field.name}
                  >
                    <ToggleButton value="asc">{t("ascending")}</ToggleButton>
                    <ToggleButton value="desc">{t("descending")}</ToggleButton>
                  </ToggleButtonGroup>
                );
              }}
            ></Controller>
            <Button
              type="submit"
              variant="contained"
              sx={{
                marginY: 2,
              }}
              startIcon={<FilterAltIcon />}
            >
              {t("filterData")}
            </Button>
          </FormComponent>
        </AccordionDetails>
      </Accordion>
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
                {t("userName")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("firstName")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("lastName")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("e-mail")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("roles")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("isActive")}
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {accounts?.accounts?.map((obj) => {
              if (obj.username === account?.username) return null;
              return (
                <AccountRowComponent
                  key={obj.id}
                  account={obj}
                ></AccountRowComponent>
              );
            })}
          </TableBody>
        </Table>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25, 50]}
          component="div"
          count={accounts?.totalElements ?? 0}
          rowsPerPage={getValues().size ?? 5}
          page={getValues().page ?? 0}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          labelRowsPerPage={t("rowsPerPage")}
          labelDisplayedRows={({ from, to, count }) => {
            return `${t("displayedRows")} ${from}-${to} ${t("of")} ${count}`;
          }}
        />
      </TableContainer>
    </ContainerComponent>
  );
}

import { LocationsWithNumberOfElements } from "../types/Location.ts";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
  Button,
  Divider,
  MenuItem,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  ToggleButton,
  ToggleButtonGroup,
  Typography,
} from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import FormComponent from "./FormComponent.tsx";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import DeletedLocationRowComponent from "./DeletedLocationRowComponent.tsx";
import { useTranslation } from "react-i18next";
import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { PaginationRequestParamsSchema } from "../validation/schemas.ts";

export default function LocationsComponent({
  locations,
  fetchLocations,
  restoreLocation,
  getDeletedLocation,
}: {
  locations: LocationsWithNumberOfElements | null;
  fetchLocations: (data: PaginationRequestParams) => void;
  restoreLocation: (id: string) => Promise<unknown>;
  getDeletedLocation: (id: string) => Promise<unknown>;
}) {
  const { t } = useTranslation();
  const [open, setOpen] = useState(false);
  const { handleSubmit, control, setValue, getValues } =
    useForm<PaginationRequestParams>({
      defaultValues: {
        page: 0,
        size: 5,
        direction: "asc",
        key: "id",
      },
      resolver: yupResolver(PaginationRequestParamsSchema),
    });

  const onSubmit: SubmitHandler<PaginationRequestParams> = async (data) => {
    await fetchLocations(data);
    setOpen(false);
  };

  const onError: SubmitErrorHandler<PaginationRequestParams> = (_) => {};

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

  useEffect(() => {
    fetchLocations(getValues());
  }, []);

  return (
    <>
      <Divider sx={{ marginTop: "1rem", marginBottom: "2rem" }} />
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Button
          variant="contained"
          sx={{
            marginY: 2,
          }}
          startIcon={<RefreshIcon />}
          onClick={() => fetchLocations(getValues())}
        >
          {t("refreshData")}
        </Button>
      </Box>
      <Accordion
        expanded={open}
        onChange={() => setOpen(!open)}
        sx={{
          marginBottom: "1rem",
        }}
      >
        <AccordionSummary expandIcon={<ArrowDownwardIcon />}>
          <Typography variant="h5">{t("sortData")}</Typography>
        </AccordionSummary>
        <AccordionDetails sx={{ padding: "1px" }}>
          <FormComponent
            handleSubmit={handleSubmit}
            onError={onError}
            onSubmit={onSubmit}
            align="start"
          >
            <Typography variant="h6">{t("sortDataKeyTitle")}</Typography>
            <Typography variant="body2">{t("sortDataKeyBody")}</Typography>
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
                    <MenuItem value="name">{t("name")}</MenuItem>
                    <MenuItem value="city">{t("city")}</MenuItem>
                    <MenuItem value="country">{t("country")}</MenuItem>
                    <MenuItem value="street">{t("street")}</MenuItem>
                    <MenuItem value="buildingNumber">
                      {t("buildingNumber")}
                    </MenuItem>
                    <MenuItem value="postalCode">{t("postalCode")}</MenuItem>
                  </TextField>
                );
              }}
            ></Controller>
            <Typography variant="h6" marginTop={3}>
              {t("sortDataDirTitle")}
            </Typography>
            <Typography variant="body2">{t("sortDataDirBody")}</Typography>
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
                marginY: 3,
              }}
              startIcon={<FilterAltIcon />}
            >
              {t("sortData")}
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
                {t("name")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("city")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("country")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("street")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("buildingNumber")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("postalCode")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("actions")}
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {locations?.locations.map((obj) => {
              return (
                <DeletedLocationRowComponent
                  key={obj.id}
                  location={obj}
                  getDeletedLocation={getDeletedLocation}
                  restoreLocation={restoreLocation}
                  fetchLocations={fetchLocations}
                  paginationRequestParams={getValues()}
                ></DeletedLocationRowComponent>
              );
            })}
          </TableBody>
        </Table>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25, 50]}
          component="div"
          count={locations?.totalElements ?? 0}
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
    </>
  );
}

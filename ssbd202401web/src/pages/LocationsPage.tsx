 import {
    Accordion,
    AccordionDetails,
    AccordionSummary, Box,
    Breadcrumbs,
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
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";
import { useLocations } from "../hooks/useLocations.ts";
import ModalComponent from "../components/ModalComponent.tsx";
import AddIcon from "@mui/icons-material/Add";
import { Link } from "react-router-dom";
import RefreshIcon from "@mui/icons-material/Refresh";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import FormComponent from "../components/FormComponent.tsx";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { PaginationRequestParamsSchema, AddLocationSchema } from "../validation/schemas.ts";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import LocationRowComponent from "../components/LocationRowComponent.tsx";
 import {CreateLocation} from "../types/Location.ts";

export default function LocationsPage() {
  const { t } = useTranslation();
  const { locations, getLocationsWithPagination, addLocation } = useLocations();
  const [open, setOpen] = useState(false);
  const {
      handleSubmit,
      control,
      setValue,
      getValues
  } = useForm<PaginationRequestParams>({
      defaultValues: {
        page: 0,
        size: 5,
        direction: "asc",
        key: "id",
      },
      resolver: yupResolver(PaginationRequestParamsSchema),
    });

    const [modalOpen, setModalOpen] = useState(false);

    const {
        handleSubmit: handleModalSubmit,
        control: modalControl,
        reset: modalReset ,
    } = useForm<CreateLocation>({
            defaultValues: {
                name: "",
                city: "",
                country: "",
                street: "",
                buildingNumber: "",
                postalCode: "",
            },
            resolver: yupResolver(AddLocationSchema),
        });

    const onModalSubmit: SubmitHandler<any> = async (data) => {
        await addLocation(data);
        setModalOpen(false);
        modalReset();
        getLocationsWithPagination(getValues());
    };

  const onSubmit: SubmitHandler<PaginationRequestParams> = async (data) => {
    await getLocationsWithPagination(data);
    setOpen(false);
  };

  useEffect(() => {
    getLocationsWithPagination(getValues());
  }, []);

  const onError: SubmitErrorHandler<PaginationRequestParams> = (error) => {
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
          to="/locations"
          style={{
            textDecoration: "none",
            color: "black",
            fontWeight: "bold",
          }}
        >
          {t("locations")}
        </Link>
        <Typography color="grey">{t("locationDetails")}</Typography>
      </Breadcrumbs>
      <Typography variant="h3">{t("manageLocations")}</Typography>
      <Divider sx={{ marginTop: "1rem", marginBottom: "2rem" }} />
        <Box display="flex" justifyContent="space-between" alignItems="center">
            <Button
                variant="contained"
                sx={{
                    marginY: 2,
                }}
                startIcon={<RefreshIcon />}
                onClick={() => getLocationsWithPagination(getValues())}
            >
                {t("refreshData")}
            </Button>
            <Button
                variant="contained"
                color="primary"
                startIcon={<AddIcon />}
                onClick={() => setModalOpen(true)}
            >
                {t("addLocation")}
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
          <Typography variant="h5">{t("filterData")}</Typography>
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
            </TableRow>
          </TableHead>
          <TableBody>
            {locations?.locations?.map((obj) => {
              return (
                <LocationRowComponent
                  key={obj.id}
                  location={obj}
                ></LocationRowComponent>
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
        <ModalComponent open={modalOpen} onClose={() => setModalOpen(false)}>
            <>
                <Typography variant="h6" component="h2">
                    {t("addLocation")}
                </Typography>
                <form onSubmit={handleModalSubmit(onModalSubmit)}>
                    <Controller
                        name="name"
                        control={modalControl}
                        render={({ field }) => (
                            <TextField
                                {...field}
                                label={t("name")}
                                fullWidth
                                margin="normal"
                            />
                        )}
                    />
                    <Controller
                        name="city"
                        control={modalControl}
                        render={({ field }) => (
                            <TextField
                                {...field}
                                label={t("city")}
                                fullWidth
                                margin="normal"
                            />
                        )}
                    />
                    <Controller
                        name="country"
                        control={modalControl}
                        render={({ field }) => (
                            <TextField
                                {...field}
                                label={t("country")}
                                fullWidth
                                margin="normal"
                            />
                        )}
                    />
                    <Controller
                        name="street"
                        control={modalControl}
                        render={({ field }) => (
                            <TextField
                                {...field}
                                label={t("street")}
                                fullWidth
                                margin="normal"
                            />
                        )}
                    />
                    <Controller
                        name="buildingNumber"
                        control={modalControl}
                        render={({ field }) => (
                            <TextField
                                {...field}
                                label={t("buildingNumber")}
                                fullWidth
                                margin="normal"
                            />
                        )}
                    />
                    <Controller
                        name="postalCode"
                        control={modalControl}
                        render={({ field }) => (
                            <TextField
                                {...field}
                                label={t("postalCode")}
                                fullWidth
                                margin="normal"
                            />
                        )}
                    />
                    <Button
                        type="submit"
                        variant="contained"
                        sx={{
                            marginTop: 2,
                        }}
                        fullWidth
                    >
                        {t("submit")}
                    </Button>
                </form>
            </>
        </ModalComponent>
    </ContainerComponent>
  );
}

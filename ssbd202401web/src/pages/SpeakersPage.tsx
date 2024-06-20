import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
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
import { useSpeakers } from "../hooks/useSpeakers.ts";
import ModalComponent from "../components/ModalComponent.tsx";
import AddIcon from "@mui/icons-material/Add";
import { Link } from "react-router-dom";
import RefreshIcon from "@mui/icons-material/Refresh";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import FormComponent from "../components/FormComponent.tsx";
import ConfirmChangeModal from "../components/ConfirmChangeModal";
import {
  Controller,
  SubmitErrorHandler,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import { useEffect, useState } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import {
  PaginationRequestParamsSchema,
  AddSpeakerSchema,
} from "../validation/schemas.ts";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import SpeakerRowComponent from "../components/SpeakerRowComponent.tsx";
import { CreateSpeaker } from "../types/Speaker.ts";
import TextFieldComponent from "../components/TextFieldComponent.tsx";

export default function SpeakersPage() {
  const { t } = useTranslation();
  const { speakers, getSpeakersWithPagination, addSpeaker } = useSpeakers();
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

  const [modalOpen, setModalOpen] = useState(false);

  const {
    handleSubmit: handleModalSubmit,
    control: modalControl,
    reset: modalReset,
    trigger,
    formState: { errors },
  } = useForm<CreateSpeaker>({
    defaultValues: {
      firstName: "",
      lastName: "",
    },
    resolver: yupResolver(AddSpeakerSchema),
  });

  const [confirmModalOpen, setConfirmModalOpen] = useState(false);
  const [pendingChanges, setPendingChanges] = useState<CreateSpeaker | null>(
    null,
  );

  const handleConfirmChanges = () => {
    if (pendingChanges) {
      addSpeaker(pendingChanges)
        .then(() => {
          setModalOpen(false);
          getSpeakersWithPagination(getValues());
        })
        .catch((_) => {});
      setPendingChanges(null);
    }
    setConfirmModalOpen(false);
  };

  const handleAddSpeaker = (data: CreateSpeaker) => {
    setPendingChanges(data);
    setConfirmModalOpen(true);
  };

  const onSubmit: SubmitHandler<PaginationRequestParams> = async (data) => {
    await getSpeakersWithPagination(data);
    setOpen(false);
  };

  useEffect(() => {
    getSpeakersWithPagination(getValues());
  }, []);

  useEffect(() => {
    if (!modalOpen) {
      modalReset();
    }
  }, [modalOpen, modalReset]);

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

  return (
    <ContainerComponent>
      <Breadcrumbs aria-label="breadcrumb" sx={{ marginBottom: 3 }}>
        <Link to="/home" style={{ textDecoration: "none", color: "black" }}>
          {t("home")}
        </Link>
        <Link
          to="/speakers"
          style={{
            textDecoration: "none",
            color: "black",
            fontWeight: "bold",
          }}
        >
          {t("speakers")}
        </Link>
        <Typography color="grey">{t("speakerDetails")}</Typography>
      </Breadcrumbs>
      <Typography variant="h3">{t("manageSpeakers")}</Typography>
      <Divider sx={{ marginTop: "1rem", marginBottom: "2rem" }} />
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Button
          variant="contained"
          sx={{
            marginY: 2,
          }}
          startIcon={<RefreshIcon />}
          onClick={() => getSpeakersWithPagination(getValues())}
        >
          {t("refreshData")}
        </Button>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => setModalOpen(true)}
        >
          {t("addSpeakerText")}
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
                    <MenuItem value="firstName">{t("firstName")}</MenuItem>
                    <MenuItem value="lastName">{t("lastName")}</MenuItem>
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
            </TableRow>
          </TableHead>
          <TableBody>
            {speakers?.speakers?.map((obj) => {
              return (
                <SpeakerRowComponent
                  key={obj.id}
                  speaker={obj}
                ></SpeakerRowComponent>
              );
            })}
          </TableBody>
        </Table>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25, 50]}
          component="div"
          count={speakers?.totalElements ?? 0}
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
      <ConfirmChangeModal
        open={confirmModalOpen}
        handleClose={() => setConfirmModalOpen(false)}
        callback={handleConfirmChanges}
      />
      <ModalComponent open={modalOpen} onClose={() => setModalOpen(false)}>
        <>
          <Typography variant="h4" component="h2">
            {t("addSpeakerText")}
          </Typography>
          <FormComponent
            handleSubmit={handleModalSubmit}
            onError={onError}
            onSubmit={handleAddSpeaker}
            align="start"
          >
            <TextFieldComponent
              control={modalControl}
              errors={errors}
              name="firstName"
              trigger={trigger}
              type="text"
              label={t("firstName")}
            />
            <TextFieldComponent
              control={modalControl}
              errors={errors}
              name="lastName"
              trigger={trigger}
              type="text"
              label={t("lastName")}
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
          </FormComponent>
        </>
      </ModalComponent>
    </ContainerComponent>
  );
}

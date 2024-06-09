import { Box, Breadcrumbs, Button, Divider, Typography } from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import RefreshIcon from "@mui/icons-material/Refresh";
import AddIcon from "@mui/icons-material/Add";
import { useAccount } from "../hooks/useAccount";
import { useState } from "react";
import ModalComponent from "../components/ModalComponent";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import FormComponent from "../components/FormComponent";
import { CreateEventType } from "../types/Event";
import { yupResolver } from "@hookform/resolvers/yup";
import { CreateEventValidationSchema } from "../validation/schemas";
import TextFieldComponent from "../components/TextFieldComponent";
import DatePickerComponent from "../components/DatePickerComponent";
import SaveIcon from "@mui/icons-material/Save";
import dayjs from "dayjs";
import { useEvents } from "../hooks/useEvents";
import ConfirmChangeModal from "../components/ConfirmChangeModal";

export default function EventsPage() {
  const { t } = useTranslation();
  const { isManager } = useAccount();
  const [modalOpen, setModalOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [redirectOpen, setRedirectOpen] = useState(false);
  const navigate = useNavigate();
  const { createEvent } = useEvents();
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    getValues,
  } = useForm<CreateEventType>({
    defaultValues: {
      description: "",
      name: "",
      startDate: dayjs(),
      endDate: dayjs(),
    },
    resolver: yupResolver(CreateEventValidationSchema),
  });

  async function handleRequest() {
    const response = await createEvent(getValues());
    if (!response) {
      setModalOpen(false);
      setRedirectOpen(true);
    }
  }

  const onSubmit: SubmitHandler<CreateEventType> = async (_) => {
    setConfirmOpen(true);
  };

  const onError: SubmitErrorHandler<CreateEventType> = (err) => {
    console.error(err);
  };

  return (
    <ContainerComponent>
      <Breadcrumbs sx={{ marginBottom: 3 }}>
        <Link
          to={Pathnames.public.home}
          style={{ textDecoration: "none", color: "black" }}
        >
          {t("home")}
        </Link>
        <Link
          to={Pathnames.public.events}
          style={{ textDecoration: "none", color: "black", fontWeight: "bold" }}
        >
          {t("eventsLink")}
        </Link>
        <Typography variant="body1" color={"grey"}>
          Wydarzenie
        </Typography>
      </Breadcrumbs>
      <Typography variant="h3">{t("eventsLink")}</Typography>
      <Divider
        sx={{
          marginTop: "1rem",
          marginBottom: "2rem",
        }}
      ></Divider>
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <Button
          variant="contained"
          color="primary"
          sx={{
            marginY: 2,
          }}
          startIcon={<RefreshIcon></RefreshIcon>}
        >
          {t("refreshData")}
        </Button>
        {isManager && (
          <Button
            variant="contained"
            color="primary"
            sx={{
              marginY: 2,
            }}
            startIcon={<AddIcon></AddIcon>}
            onClick={() => setModalOpen(true)}
          >
            {t("addEvent")}
          </Button>
        )}
      </Box>
      <ModalComponent
        width={700}
        onClose={() => setModalOpen(false)}
        open={modalOpen}
      >
        <FormComponent
          handleSubmit={handleSubmit}
          onError={onError}
          onSubmit={onSubmit}
        >
          <Typography align="center" variant="h4">
            {t("addEventLabel")}
          </Typography>
          <TextFieldComponent
            control={control}
            errors={errors}
            label={t("eventNameLabel") + "*"}
            name="name"
            trigger={trigger}
            type="text"
          ></TextFieldComponent>
          <TextFieldComponent
            control={control}
            errors={errors}
            label={t("eventDescLabel") + "*"}
            name="description"
            trigger={trigger}
            type="text"
            multiline={true}
            rows={5}
          ></TextFieldComponent>
          <DatePickerComponent
            control={control}
            errors={errors}
            label={t("startDateLabel") + "*"}
            name="startDate"
            trigger={trigger}
            type=""
            whatToValidate={["endDate"]}
          ></DatePickerComponent>
          <DatePickerComponent
            control={control}
            errors={errors}
            label={t("endDateLabel") + "*"}
            name="endDate"
            trigger={trigger}
            type=""
            whatToValidate={["startDate"]}
          ></DatePickerComponent>
          <Button
            type="submit"
            variant="contained"
            startIcon={<SaveIcon></SaveIcon>}
            sx={{
              mt: 1,
              mb: 2,
              width: "fit-content",
              alignSelf: "center",
            }}
          >
            {t("saveChanges")}
          </Button>
        </FormComponent>
      </ModalComponent>
      <ConfirmChangeModal
        open={confirmOpen}
        callback={handleRequest}
        handleClose={() => setConfirmOpen(false)}
      ></ConfirmChangeModal>
      <ModalComponent
        onClose={() => setRedirectOpen(false)}
        open={redirectOpen}
        width={600}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            gap: "1rem",
          }}
        >
          <Typography align="center" variant="h4">
            {t("createEventSucc")}
          </Typography>
          <Typography align="center" variant="body1">
            {t("eventRedirectQuestion")}
          </Typography>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <Button
              onClick={() => {
                navigate("/events/" + getValues().name.split(" ").join("+"));
              }}
              color={"success"}
            >
              {t("yes")}
            </Button>
            <Button
              onClick={() => {
                setRedirectOpen(false);
              }}
              color={"error"}
            >
              {t("no")}
            </Button>
          </Box>
        </Box>
      </ModalComponent>
    </ContainerComponent>
  );
}

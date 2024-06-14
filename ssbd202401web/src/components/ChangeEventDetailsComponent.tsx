import { useTranslation } from "react-i18next";
import { CreateEventType, Event, UpdateEventType } from "../types/Event";
import { useEvents } from "../hooks/useEvents";
import { yupResolver } from "@hookform/resolvers/yup";
import { useEffect, useState } from "react";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { UpdateEventValidationSchema } from "../validation/schemas";
import dayjs from "dayjs";
import FormComponent from "./FormComponent";
import { Button, Divider, Typography } from "@mui/material";
import TextFieldComponent from "./TextFieldComponent";
import DatePickerComponent from "./DatePickerComponent";
import SaveIcon from "@mui/icons-material/Save";
import ConfirmChangeModal from "./ConfirmChangeModal";
import { CancelEventComponent } from "./CancelEventComponent.tsx";

export default function ChangeEventDetailsComponent({
  event,
  getEvent,
}: {
  event: Event | null;
  getEvent: () => void;
}) {
  const { t } = useTranslation();
  const { updateEvent } = useEvents();
  const [open, setOpen] = useState(false);
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
    getValues,
  } = useForm<UpdateEventType>({
    defaultValues: {
      description: event?.description ?? "",
      name: event?.name ?? "",
      startDate: dayjs(event?.startDate) ?? dayjs(),
      endDate: dayjs(event?.endDate) ?? dayjs(),
    },
    resolver: yupResolver(UpdateEventValidationSchema),
  });

  async function handleRequest() {
    const response = await updateEvent(event?.id ?? "", getValues());
    if (!response) {
      getEvent();
    }
  }

  const onSubmit: SubmitHandler<CreateEventType> = async (_) => {
    setOpen(true);
  };

  const onError: SubmitErrorHandler<CreateEventType> = (err) => {
    console.error(err);
  };

  useEffect(() => {
    setValue("name", event?.name ?? "");
    setValue("description", event?.description ?? "");
    setValue("startDate", dayjs(event?.startDate) ?? dayjs());
    setValue("endDate", dayjs(event?.endDate) ?? dayjs());
  }, [event]);

  return (
    <>
      <FormComponent
        handleSubmit={handleSubmit}
        onError={onError}
        onSubmit={onSubmit}
        align="start"
      >
        <Typography variant="h4">{t("changeEventTitle")}</Typography>
        <Typography variant="body1">{t("changeEventBody")}</Typography>
        <Divider
          sx={{
            marginTop: "1rem",
          }}
        ></Divider>
        <TextFieldComponent
          control={control}
          errors={errors}
          name="name"
          label={t("eventNameLabel") + "*"}
          trigger={trigger}
          type="text"
        ></TextFieldComponent>
        <TextFieldComponent
          control={control}
          errors={errors}
          name="description"
          label={t("eventDescLabel") + "*"}
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
            alignSelf: "start",
          }}
        >
          {t("saveChanges")}
        </Button>
        {event?.isNotCanceled && (
          <CancelEventComponent id={event?.id ?? ""}></CancelEventComponent>
        )}
      </FormComponent>
      <ConfirmChangeModal
        callback={handleRequest}
        handleClose={() => setOpen(false)}
        open={open}
      ></ConfirmChangeModal>
    </>
  );
}

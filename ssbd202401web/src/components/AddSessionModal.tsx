import { Button, Typography } from "@mui/material";
import ModalComponent from "./ModalComponent";
import { yupResolver } from "@hookform/resolvers/yup";
import dayjs from "dayjs";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { CreateSessionSchema } from "../validation/schemas";
import { CreateSessionType } from "../types/Session";
import { useTranslation } from "react-i18next";
import { useSessions } from "../hooks/useSessions";
import FormComponent from "./FormComponent";
import { useEffect, useState } from "react";
import TextFieldComponent from "./TextFieldComponent";
import AutocompletionComponent from "./AutocompletionComponent";
import SaveIcon from "@mui/icons-material/Save";
import ConfirmChangeModal from "./ConfirmChangeModal";
import DateTimePickerComponent from "./DateTimePickerComponent";
import { Event } from "../types/Event";

export default function AddSessionModal({
  open,
  onClose,
  eventId,
  getSessions,
  event,
}: {
  open: boolean;
  onClose: () => void;
  eventId: string | null;
  getSessions: () => void;
  event: Event | null;
}) {
  const { t } = useTranslation();
  const { createSession } = useSessions();
  const [openConfirm, setOpenConfirm] = useState(false);

  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
    getValues,
  } = useForm<CreateSessionType>({
    defaultValues: {
      name: "",
      description: "",
      eventId: eventId ?? "",
      speakerId: "",
      roomId: "",
      startDate: dayjs(event?.startDate),
      endDate: dayjs(event?.endDate),
      maxSeats: 1,
      locationId: "",
    },
    resolver: yupResolver(CreateSessionSchema),
  });

  useEffect(() => {
    setValue("startDate", dayjs(event?.startDate));
    setValue("endDate", dayjs(event?.endDate));
  }, [event]);

  const setSpeakerId = (speakerId: string) => {
    setValue("speakerId", speakerId);
  };

  const setRoomId = (roomId: string) => {
    setValue("roomId", roomId);
  };

  const setLocationId = (locationId: string) => {
    setValue("locationId", locationId);
  };

  const handleRequest = async () => {
    const response = await createSession(getValues());
    if (!response) {
      setOpenConfirm(false);
      onClose();
      getSessions();
    }
  };

  const onSubmit: SubmitHandler<CreateSessionType> = async (_) => {
    setOpenConfirm(true);
  };

  const onError: SubmitErrorHandler<CreateSessionType> = (_) => {};

  return (
    <>
      <ModalComponent onClose={onClose} open={open} width={600}>
        <FormComponent
          handleSubmit={handleSubmit}
          onSubmit={onSubmit}
          onError={onError}
          align="center"
        >
          <Typography variant="h4">{t("addSession")}</Typography>
          <TextFieldComponent
            control={control}
            errors={errors}
            name="name"
            label={t("sessionNameLabel") + "*"}
            trigger={trigger}
            type="text"
          ></TextFieldComponent>
          <TextFieldComponent
            control={control}
            errors={errors}
            name="description"
            label={t("sessionDescLabel") + "*"}
            trigger={trigger}
            type="text"
            multiline
            rows={3}
          ></TextFieldComponent>
          <AutocompletionComponent
            setRoomId={setRoomId}
            setSpeakerId={setSpeakerId}
            setLocationId={setLocationId}
            errors={errors}
            trigger={trigger}
          ></AutocompletionComponent>
          <TextFieldComponent
            control={control}
            errors={errors}
            name="maxSeats"
            label={t("sessionMaxSeatsLabel") + "*"}
            trigger={trigger}
            type="number"
          ></TextFieldComponent>
          <DateTimePickerComponent
            control={control}
            errors={errors}
            label={t("startDateLabel") + "*"}
            name="startDate"
            trigger={trigger}
            type=""
            whatToValidate={["endDate"]}
            minDate={dayjs(event?.startDate)}
            maxDate={dayjs(event?.endDate)}
          ></DateTimePickerComponent>
          <DateTimePickerComponent
            control={control}
            errors={errors}
            label={t("endDateLabel") + "*"}
            name="endDate"
            trigger={trigger}
            type=""
            whatToValidate={["startDate"]}
            minDate={dayjs(event?.startDate)}
            maxDate={dayjs(event?.endDate)}
          ></DateTimePickerComponent>
          <Button
            type="submit"
            variant="contained"
            startIcon={<SaveIcon></SaveIcon>}
            sx={{
              mt: 1,
              mb: 2,
              width: "fit-content",
            }}
          >
            {t("saveChanges")}
          </Button>
        </FormComponent>
      </ModalComponent>
      <ConfirmChangeModal
        callback={() => handleRequest()}
        handleClose={() => setOpenConfirm(false)}
        open={openConfirm}
      ></ConfirmChangeModal>
    </>
  );
}

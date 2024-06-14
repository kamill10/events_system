import { yupResolver } from "@hookform/resolvers/yup";
import dayjs from "dayjs";
import { useEffect, useState } from "react";
import { useForm, SubmitHandler, SubmitErrorHandler } from "react-hook-form";
import { useTranslation } from "react-i18next";
import { useSessions } from "../hooks/useSessions";
import { CreateSessionType, UpdateSessionType } from "../types/Session";
import { CreateSessionSchema } from "../validation/schemas";
import { SessionDetailedType } from "../types/SessionDetailed";
import { AxiosError } from "axios";
import { Typography, Button } from "@mui/material";
import AutocompletionComponent from "./AutocompletionComponent";
import ConfirmChangeModal from "./ConfirmChangeModal";
import DateTimePickerComponent from "./DateTimePickerComponent";
import FormComponent from "./FormComponent";
import SaveIcon from "@mui/icons-material/Save";
import TextFieldComponent from "./TextFieldComponent";

export default function UpdateSessionComponent({ id }: { id: string }) {
  const { t } = useTranslation();
  const { getSessionForManager, updateSession } = useSessions();
  const [openConfirm, setOpenConfirm] = useState(false);
  const [session, setSession] = useState<SessionDetailedType | null>(null);

  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
    getValues,
  } = useForm<UpdateSessionType>({
    defaultValues: {
      name: session?.name ?? "",
      description: session?.description ?? "",
      eventId: id ?? "",
      speakerId: session?.speaker.id ?? "",
      roomId: session?.room.id ?? "",
      startDate: session?.startTime ? dayjs(session.startTime) : dayjs(),
      endDate: session?.endTime ? dayjs(session.endTime) : dayjs(),
      maxSeats: session?.maxSeats ?? 1,
      locationId: session?.room.location.id ?? "",
    },
    resolver: yupResolver(CreateSessionSchema),
  });

  const fetchSession = async () => {
    const response = await getSessionForManager(id ?? "");
    if (!(response instanceof AxiosError)) {
      setSession(response as SessionDetailedType);
    }
  };

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
    const response = await updateSession(id, getValues());
    if (!response) {
      setOpenConfirm(false);
      fetchSession();
    }
  };

  const onSubmit: SubmitHandler<CreateSessionType> = async (_) => {
    setOpenConfirm(true);
  };

  const onError: SubmitErrorHandler<CreateSessionType> = (err) => {
    console.log(getValues());
    console.error(err);
  };

  useEffect(() => {
    fetchSession();
  }, []);
  useEffect(() => {
    setValue("description", session?.description ?? "");
    setValue("name", session?.name ?? "");
    setValue("eventId", session?.event.id ?? "");
    setValue(
      "startDate",
      session?.startTime ? dayjs(session.startTime) : dayjs(),
    );
    setValue("endDate", session?.endTime ? dayjs(session.endTime) : dayjs());
    setValue("maxSeats", session?.maxSeats ?? 1);
    setValue("speakerId", session?.speaker.id ?? "");
    setValue("locationId", session?.room.location.id ?? "");
    setValue("roomId", session?.room.id ?? "");
  }, [session]);

  return (
    <>
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
        ></TextFieldComponent>
        <AutocompletionComponent
          setRoomId={setRoomId}
          setSpeakerId={setSpeakerId}
          setLocationId={setLocationId}
          errors={errors}
          trigger={trigger}
          update
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
        ></DateTimePickerComponent>
        <DateTimePickerComponent
          control={control}
          errors={errors}
          label={t("endDateLabel") + "*"}
          name="endDate"
          trigger={trigger}
          type=""
          whatToValidate={["startDate"]}
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
      <ConfirmChangeModal
        callback={() => handleRequest()}
        handleClose={() => setOpenConfirm(false)}
        open={openConfirm}
      ></ConfirmChangeModal>
    </>
  );
}

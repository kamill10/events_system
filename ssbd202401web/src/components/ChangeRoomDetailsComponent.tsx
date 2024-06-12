import { useTranslation } from "react-i18next";
import { useLocations } from "../hooks/useLocations.ts";
import { useEffect, useState } from "react";
import { RoomType, UpdateRoomType } from "../types/Room.ts";
import { SubmitErrorHandler, SubmitHandler, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { UpdateRoomSchema } from "../validation/schemas.ts";
import DeleteIcon from '@mui/icons-material/Delete';
import FormComponent from "./FormComponent.tsx";
import { Button, Divider, Typography } from "@mui/material";
import TextFieldComponent from "./TextFieldComponent.tsx";
import ConfirmChangeModal from "./ConfirmChangeModal.tsx";

export default function ChangeRoomDetailsComponent({
  room,
  fetchRoom,
  setOpen,
  getRooms,
}: {
  room: RoomType | null;
  fetchRoom: () => void;
  setOpen: (open: boolean) => void;
  getRooms: () => void;
}) {
  const { t } = useTranslation();
  const { updateRoomById, deleteRoomById } = useLocations();
  const [confirmOpen, setConfirmOpen] = useState(false);
  const {
    handleSubmit,
    control,
    formState: { errors },
    trigger,
    setValue,
    getValues,
  } = useForm<UpdateRoomType>({
    defaultValues: {
      name: room?.name ?? "",
      maxCapacity: room?.maxCapacity ?? 1,
    },
    resolver: yupResolver(UpdateRoomSchema),
  });
  const handleRequest = async () => {
    await updateRoomById(room?.id ?? "", getValues());
    getRooms();
    setConfirmOpen(false);
    setOpen(false);
  };
  const onSubmit: SubmitHandler<UpdateRoomType> = async () => {
    setConfirmOpen(true);
    fetchRoom();
  };
  const onError: SubmitErrorHandler<UpdateRoomType> = (error) => {
    console.error(error);
  };

  useEffect(() => {
    setValue("name", room?.name ?? "");
    setValue("maxCapacity", room?.maxCapacity ?? 1);
    console.log(room?.name, room?.maxCapacity);
  }, [room, setValue, trigger]);

  const handleDelete = () => {
    deleteRoomById(room?.id ?? "");
  }
  
  return (
    <>
      <FormComponent
        handleSubmit={handleSubmit}
        onSubmit={onSubmit}
        onError={onError}
        align="start"
      >
        <Typography variant="h4">{t("changeRoomDetails")}</Typography>
        <Typography variant="body1">{t("enterNewRoomData")}</Typography>
        <Divider
          sx={{
            marginTop: "1rem",
          }}
        ></Divider>
        <TextFieldComponent
          control={control}
          errors={errors}
          name="name"
          label={t("roomName") + "*"}
          trigger={trigger}
          type="text"
        />
        <TextFieldComponent
          control={control}
          errors={errors}
          name="maxCapacity"
          label={t("maxCapacity") + "*"}
          trigger={trigger}
          type="number"
        />
        <Button
          type="submit"
          variant="contained"
          sx={{
            marginY: 2,
          }}
        >
          {t("saveChanges")}
        </Button>
        <Button
        color="error"
        variant="contained"
        startIcon={<DeleteIcon/>}
        onClick={handleDelete}
        >
          {t("delete")}
        </Button>
      </FormComponent>
      <ConfirmChangeModal
        callback={handleRequest}
        handleClose={() => setConfirmOpen(false)}
        open={confirmOpen}
      ></ConfirmChangeModal>
    </>
  );
}

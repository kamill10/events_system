import { TableCell, TableRow} from "@mui/material";
import {RoomType} from "../types/Room.ts";
import ChangeRoomDetailsComponent from "./ChangeRoomDetailsComponent.tsx";
import {useState} from "react";
import useNotification from "../hooks/useNotification.tsx";
import {useLocations} from "../hooks/useLocations.ts";
import {useTranslation} from "react-i18next";
import ModalComponent from "./ModalComponent.tsx";

export default function RoomRowComponent({
                                             room,getRooms }
                                             : { room: RoomType,getRooms : () => void}) {
    const [open, setOpen] = useState(false);
    const sendNotification = useNotification();
    const {getRoomById} = useLocations();
    const {t} = useTranslation();
    const handleClick = () => {
        setOpen(true);
    };
    async function fetchRoom() {
        if (room.id) {
            await getRoomById(room.id);
        } else {
            sendNotification({
                type: "error",
                description: t("getRoomByFail"),
            });
        }
    }
    return (
        <>
            <TableRow hover onClick={handleClick}>
                <TableCell>{room.id}</TableCell>
                <TableCell align="right">{room.name}</TableCell>
                <TableCell align="right">{room.maxCapacity}</TableCell>
            </TableRow>
            <ModalComponent open={open} onClose={()=>setOpen(false)}  width={400}>
                <ChangeRoomDetailsComponent room={room} fetchRoom={fetchRoom} setOpen={setOpen} getRooms={getRooms}  />
            </ModalComponent>
        </>
    );
}

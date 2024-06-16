import {ActionTypeEnum} from "./enums/ActionType.enum.ts";

export interface SpeakerChanges {
    id: string;
  firstName: string;
  lastName: string;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
  actionType: ActionTypeEnum;

}
import ChangeSpeakerDataComponent from "./ChangeSpeakerDataComponent.tsx";
import { Speaker } from "../types/Speaker.ts";

export default function ChangeSpeakerDetails({
  speaker,
  fetchSpeaker,
}: {
  speaker: Speaker | null;
  fetchSpeaker: () => void;
}) {
  return (
    <>
      <ChangeSpeakerDataComponent
        speaker={speaker}
        fetchSpeaker={fetchSpeaker}
      />
    </>
  );
}

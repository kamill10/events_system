import { Box, Typography } from "@mui/material";

export default function ElementComponent({ label, value, color }: {label: string, value: any, color?: string}) {
  return (
    <Box
        sx={{
          marginBottom: "1rem",
        }}
      >
        <Typography variant="h5">{label}</Typography>
        <Typography variant="body1" sx={{
          color: color ?? "grey"
        }}>{value}</Typography>
      </Box>
  )
}
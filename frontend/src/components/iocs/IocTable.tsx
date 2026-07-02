import {
    Button,
    Chip,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
} from "@mui/material";

import type { IndicatorOfCompromise } from "../../types/ioc";

interface Props {
    iocs: IndicatorOfCompromise[];
    onEdit: (ioc: IndicatorOfCompromise) => void;
    onDelete: (ioc: IndicatorOfCompromise) => void;
    onToggleActive: (ioc: IndicatorOfCompromise) => void;
}

export default function IocTable({
                                     iocs,
                                     onEdit,
                                     onDelete,
                                     onToggleActive,
                                 }: Props) {
    return (
        <TableContainer
            component={Paper}
            sx={{
                bgcolor: "#111827",
                border: "1px solid #1f2937",
                borderRadius: 3,
            }}
        >
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Status</TableCell>
                        <TableCell>Type</TableCell>
                        <TableCell>Value</TableCell>
                        <TableCell>Confidence</TableCell>
                        <TableCell>Threat Source</TableCell>
                        <TableCell>Malware Family</TableCell>
                        <TableCell>Campaign</TableCell>
                        <TableCell>Created</TableCell>
                        <TableCell>Actions</TableCell>
                    </TableRow>
                </TableHead>

                <TableBody>
                    {iocs.length === 0 ? (
                        <TableRow>
                            <TableCell colSpan={9}>
                                <Typography sx={{ color: "#9ca3af" }}>
                                    No IOCs found.
                                </Typography>
                            </TableCell>
                        </TableRow>
                    ) : (
                        iocs.map((ioc) => (
                            <TableRow key={ioc.id} hover>
                                <TableCell>
                                    <Chip
                                        label={ioc.active ? "ACTIVE" : "DISABLED"}
                                        color={ioc.active ? "success" : "default"}
                                        size="small"
                                    />
                                </TableCell>

                                <TableCell>{ioc.type}</TableCell>

                                <TableCell
                                    sx={{
                                        fontFamily: "monospace",
                                        maxWidth: 280,
                                        overflowWrap: "anywhere",
                                    }}
                                >
                                    {ioc.value}
                                </TableCell>

                                <TableCell>{ioc.confidence ?? 0}%</TableCell>
                                <TableCell>{ioc.threatSource || "N/A"}</TableCell>
                                <TableCell>{ioc.malwareFamily || "N/A"}</TableCell>
                                <TableCell>{ioc.campaign || "N/A"}</TableCell>

                                <TableCell>
                                    {ioc.createdAt
                                        ? new Date(ioc.createdAt).toLocaleString()
                                        : "N/A"}
                                </TableCell>

                                <TableCell>
                                    <Stack direction="row" spacing={1}>
                                        <Button
                                            size="small"
                                            variant="outlined"
                                            onClick={() => onEdit(ioc)}
                                        >
                                            Edit
                                        </Button>

                                        <Button
                                            size="small"
                                            variant="outlined"
                                            color={ioc.active ? "warning" : "success"}
                                            onClick={() => onToggleActive(ioc)}
                                        >
                                            {ioc.active ? "Disable" : "Enable"}
                                        </Button>

                                        <Button
                                            size="small"
                                            variant="outlined"
                                            color="error"
                                            onClick={() => onDelete(ioc)}
                                        >
                                            Delete
                                        </Button>
                                    </Stack>
                                </TableCell>
                            </TableRow>
                        ))
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    );
}
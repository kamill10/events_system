import {
    Breadcrumbs,
    Button,
    Divider,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TextField,
    TableHead,
    TableRow,
    Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import { useManageAccounts } from "../hooks/useManageAccounts";
import AccountRowComponent from "../components/AccountRowComponent";
import ContainerComponent from "../components/ContainerComponent";
import { Link } from "react-router-dom";
import RefreshIcon from "@mui/icons-material/Refresh";
import { useAccount } from "../hooks/useAccount";
import { useTranslation } from "react-i18next";
import {SortingRequestParams} from "../types/SortingRequestParams.ts";

export default function AccountsPage() {
    const { t } = useTranslation();
    const { accounts, getAccountsWithPagination } = useManageAccounts();
    const { account } = useAccount();

    const [requestParams, setRequestParams] = useState<SortingRequestParams>({
        page: 0,
        size: 5,
        direction: 'asc',
        key: 'id',
        phrase: '',
    });

    const handlePhraseChange = (event : any) => {
        const { value } = event.target;
        setRequestParams((prevParams) => ({
            ...prevParams,
            page: 0,
            phrase: value,
        }));
    };

    const searchAccounts = () => {
        getAccountsWithPagination(requestParams);
    };

    useEffect(() => {
        getAccountsWithPagination(requestParams);
    }, [requestParams]);

    return (
        <ContainerComponent>
            <Breadcrumbs aria-label="breadcrumb" sx={{ marginBottom: 3 }}>
                <Link to="/home" style={{ textDecoration: 'none', color: 'black' }}>
                    {t('home')}
                </Link>
                <Link
                    to="/admin/accounts"
                    style={{
                        textDecoration: 'none',
                        color: 'black',
                        fontWeight: 'bold',
                    }}
                >
                    {t('accounts')}
                </Link>
                <Typography color="grey">{t('accountDetails')}</Typography>
            </Breadcrumbs>
            <Typography variant="h3">{t('manageAccounts')}</Typography>
            <Divider sx={{ marginTop: '1rem', marginBottom: '2rem' }} />
            <div style={{ display: 'flex', alignItems: 'flex-end', marginBottom: '1rem' }}>
                <TextField
                    label={t('searchPhrase')}
                    variant="outlined"
                    value={requestParams.phrase}
                    onChange={handlePhraseChange}
                    fullWidth
                    sx={{ marginRight: '1rem' }}
                />
                <Button
                    variant="contained"
                    startIcon={<RefreshIcon />}
                    onClick={searchAccounts}
                    sx={{ marginLeft: '1rem', paddingX: '1rem' }}
                >
                    {t('refreshData')}
                </Button>
            </div>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow hover>
                            <TableCell
                                sx={{
                                    fontWeight: 'bold',
                                    fontSize: '18px',
                                }}
                            >
                                ID
                            </TableCell>
                            <TableCell
                                align="right"
                                sx={{
                                    fontWeight: 'bold',
                                    fontSize: '18px',
                                }}
                            >
                                {t('userName')}
                            </TableCell>
                            <TableCell
                                align="right"
                                sx={{
                                    fontWeight: 'bold',
                                    fontSize: '18px',
                                }}
                            >
                                {t('firstName')}
                            </TableCell>
                            <TableCell
                                align="right"
                                sx={{
                                    fontWeight: 'bold',
                                    fontSize: '18px',
                                }}
                            >
                                {t('lastName')}
                            </TableCell>
                            <TableCell
                                align="right"
                                sx={{
                                    fontWeight: 'bold',
                                    fontSize: '18px',
                                }}
                            >
                                {t('e-mail')}
                            </TableCell>
                            <TableCell
                                align="right"
                                sx={{
                                    fontWeight: 'bold',
                                    fontSize: '18px',
                                }}
                            >
                                {t('roles')}
                            </TableCell>
                            <TableCell
                                align="right"
                                sx={{
                                    fontWeight: 'bold',
                                    fontSize: '18px',
                                }}
                            >
                                {t('isActive')}
                            </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {accounts?.accounts?.map((obj) => {
                            if (obj.username === account?.username) return null;
                            return (
                                <AccountRowComponent
                                    key={obj.id}
                                    account={obj}
                                ></AccountRowComponent>
                            );
                        })}
                    </TableBody>
                </Table>
            </TableContainer>
        </ContainerComponent>
    );
}

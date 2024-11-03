import React, { useEffect, useState } from "react";
import { getAccountBook, GetAccountBook } from "../apis/api/getaccountbook";
import Navbar from "../Components/Navbar";
import { Line } from "react-chartjs-2";
import { Doughnut } from "react-chartjs-2";
import {
  Chart as ChartJS,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  ArcElement,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  ArcElement,
  Tooltip,
  Legend
);
import * as XLSX from "xlsx";
import excel from "../assets/images/excel.png";
import { TooltipItem } from "chart.js";

const AccountBook: React.FC = () => {
  const [accountData, setAccountData] = useState<GetAccountBook | null>(null);
  const [sortedData, setSortedData] = useState<GetAccountBook | null>(null);
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [itemsPerPage] = useState<number>(10);

  const accessToken = localStorage.getItem("accessToken");

  const exportToExcel = () => {
    if (!sortedData) return;

    const accountData = sortedData.data.map((item, index) => ({
      순번: index + 1,
      성함: item.memberInfo.nickname,
      "금액(원)": item.charge.toLocaleString(),
      구분: item.isBride === true ? "신부" : "신랑",
      메시지: item.message || "메시지가 없습니다.",
    }));

    const totalSummary = [
      { 구분: "총 합계", 금액: totalCharge?.toLocaleString() || 0 },
      { 구분: "신부측 합계", 금액: brideTotal?.toLocaleString() || 0 },
      { 구분: "신랑측 합계", 금액: groomTotal?.toLocaleString() || 0 },
    ];

    const accountWorksheet = XLSX.utils.json_to_sheet(accountData);
    const summaryWorksheet = XLSX.utils.json_to_sheet(totalSummary);

    const workbook = XLSX.utils.book_new();

    XLSX.utils.book_append_sheet(workbook, accountWorksheet, "AccountDetails");
    XLSX.utils.book_append_sheet(workbook, summaryWorksheet, "TotalSummary");

    XLSX.writeFile(workbook, "account_book.xlsx");
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (accessToken) {
          const data = await getAccountBook(accessToken);
          setAccountData(data);
          setSortedData(data);
        }
      } catch (err) {
        console.error(err);
      }
    };

    fetchData();
  }, [accessToken]);

  const totalCharge = accountData?.data.reduce(
    (acc, item) => acc + item.charge,
    0
  );

  const brideTotal = accountData?.data
    .filter((item) => item.isBride === true)
    .reduce((acc, item) => acc + item.charge, 0);

  const groomTotal = accountData?.data
    .filter((item) => item.isBride === false)
    .reduce((acc, item) => acc + item.charge, 0);

  const chargeCountMap: { [charge: number]: number } = {};

  sortedData?.data.forEach((item) => {
    const charge = item.charge;
    if (chargeCountMap[charge]) {
      chargeCountMap[charge] += 1;
    } else {
      chargeCountMap[charge] = 1;
    }
  });

  const chartColors = ["hsl(344, 100%, 90%)", "hsl(236, 100%, 80%)"];

  const sortedLabels = Object.keys(chargeCountMap)
    .map(Number)
    .sort((a, b) => (sortOrder === "asc" ? a - b : b - a))
    .map((label) => label.toLocaleString());

  const brideData =
    sortedData?.data.filter((item) => item.isBride === true) || [];
  const groomData =
    sortedData?.data.filter((item) => item.isBride === false) || [];

  const chartData = {
    labels: sortedLabels,
    datasets: [
      {
        label: "신부측 인원 수",
        data: sortedLabels.map(
          (label) =>
            brideData.filter(
              (item) => item.charge === parseInt(label.replace(/,/g, ""))
            ).length
        ),
        borderColor: "hsl(344, 100%, 90%)",
        backgroundColor: "hsl(344, 100%, 70%)",
        fill: false,
      },
      {
        label: "신랑측 인원 수",
        data: sortedLabels.map(
          (label) =>
            groomData.filter(
              (item) => item.charge === parseInt(label.replace(/,/g, ""))
            ).length
        ),
        borderColor: "hsl(236, 100%, 80%)",
        backgroundColor: "hsl(236, 100%, 60%)",
        fill: false,
      },
    ],
  };
  const doughnutData = {
    labels: ["신부측", "신랑측"],
    datasets: [
      {
        data: [brideTotal || 0, groomTotal || 0],
        backgroundColor: chartColors,
        hoverOffset: 4,
      },
    ],
  };

  const chartOptions = {
    plugins: {
      tooltip: {
        callbacks: {
          label: (tooltipItem: any) => {
            const dataset = tooltipItem.dataset;
            const total = dataset.data.reduce(
              (acc: number, value: number) => acc + value,
              0
            );
            const currentValue = dataset.data[tooltipItem.dataIndex];
            const percentage = ((currentValue / total) * 100).toFixed(2);
            return `인원 수: ${currentValue}명 (${percentage}%)`;
          },
        },
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: "금액 (원)",
        },
      },
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: "인원 수 (명)",
        },
        ticks: {
          stepSize: 1,
          precision: 0,
        },
      },
    },
  };

  const doughnutOptions = {
    plugins: {
      tooltip: {
        callbacks: {
          label: (tooltipItem: TooltipItem<"doughnut">) => {
            const dataset = tooltipItem.dataset;
            const total = dataset.data.reduce(
              (acc: number, value: number) => acc + value,
              0
            );
            const currentValue = dataset.data[tooltipItem.dataIndex];
            const percentage = ((currentValue / total) * 100).toFixed(2);
            return `${currentValue.toLocaleString()}원 (${percentage}%)`;
          },
        },
      },
    },
  };

  const handleSortByCharge = () => {
    if (!accountData) return;

    const sorted = [...accountData.data].sort((a, b) => {
      if (sortOrder === "asc") {
        return b.charge - a.charge;
      } else {
        return a.charge - b.charge;
      }
    });

    setSortedData({ ...accountData, data: sorted });
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = sortedData?.data.slice(
    indexOfFirstItem,
    indexOfLastItem
  );

  const totalPages = sortedData
    ? Math.ceil(sortedData.data.length / itemsPerPage)
    : 0;

  return (
    <div className="font-nanum">
      <Navbar isScrollSensitive={false} />
      <div className="font-default mt-24 mb-6 min-w-[1200px] flex flex-wrap">
        {sortedData && sortedData.data.length > 0 ? (
          <div className="flex flex-col w-full">
            <div className="flex flex-1 gap-6">
              <div className="bg-white shadow-md rounded-lg p-4 col-span-1 w-[560px] justify-between h-full relative">
                {" "}
                <div className="flex justify-between">
                  <h2 className="text-md font-bold">청첩장 장부 내역</h2>
                  <button
                    onClick={exportToExcel}
                    className="px-2 py-2 bg-gray-200 shadow-md rounded"
                  >
                    <img src={excel} alt="excel" className="w-5 h-5" />
                  </button>
                </div>
                <div className="flex justify-center">
                  <table className="table-auto border-collapse w-full text-left mt-2">
                    <thead>
                      <tr>
                        <th className="border-b px-4 py-2">순번</th>
                        <th className="border-b px-4 py-2">성함</th>
                        <th
                          className="border-b px-4 py-2 cursor-pointer"
                          onClick={handleSortByCharge}
                        >
                          금액(원)
                          {sortOrder === "asc" ? " ▲" : " ▼"}
                        </th>
                        <th className="border-b px-4 py-2">구분</th>
                      </tr>
                    </thead>
                    <tbody>
                      {currentItems?.map((item, index) => (
                        <tr key={item.id} className="hover:bg-gray-100">
                          <td className="border-b px-4 py-2">
                            {currentPage === 1
                              ? index + 1
                              : (currentPage - 1) * itemsPerPage + index + 1}
                          </td>
                          <td className="border-b px-4 py-2">
                            {item.memberInfo.nickname}
                          </td>
                          <td className="border-b px-4 py-2">
                            {item.charge.toLocaleString()}
                          </td>
                          <td className="border-b px-4 py-2">
                            {item.isBride === true
                              ? "신부"
                              : item.isBride === false
                              ? "신랑"
                              : "신랑신부 모두"}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
                <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex">
                  {Array.from({ length: totalPages }, (_, index) => (
                    <button
                      key={index + 1}
                      onClick={() => setCurrentPage(index + 1)}
                      className={`mx-1 px-3 py-1 rounded ${
                        currentPage === index + 1
                          ? "bg-gray-700 text-white"
                          : "bg-gray-200"
                      }`}
                    >
                      {index + 1}
                    </button>
                  ))}
                </div>
              </div>

              <div>
                <div className="mb-5">
                  <div className="flex flex-1 gap-5">
                    <div className="bg-white shadow-md rounded-lg p-4 col-span-1 h-[280px] flex flex-col w-[320px]">
                      <h2 className="text-md mb-4 font-bold">
                        전체 축의금 비율
                      </h2>
                      <div className="flex-grow h-[180px] flex items-center justify-center">
                        {" "}
                        <Doughnut
                          data={doughnutData}
                          options={doughnutOptions}
                        />
                      </div>
                    </div>

                    <div className="bg-white shadow-md rounded-lg p-4 w-[320px] h-auto flex flex-col">
                      {" "}
                      <h2 className="text-md mb-5 font-bold">금액 합계</h2>
                      <div className="flex-grow">
                        {" "}
                        <table className="table-auto border-collapse w-full text-left mt-2">
                          <thead>
                            <tr>
                              <th className="border-b px-4 py-2">구분</th>
                              <th className="border-b px-4 py-2">금액(원)</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td className="border-b px-4 py-2">총 합계</td>
                              <td className="border-b px-4 py-2">
                                {totalCharge?.toLocaleString() || 0}
                              </td>
                            </tr>
                            <tr>
                              <td className="border-b px-4 py-2">
                                신부측 합계
                              </td>
                              <td className="border-b px-4 py-2">
                                {brideTotal?.toLocaleString() || 0}
                              </td>
                            </tr>
                            <tr>
                              <td className="border-b px-4 py-2">
                                신랑측 합계
                              </td>
                              <td className="border-b px-4 py-2">
                                {groomTotal?.toLocaleString() || 0}
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="bg-white shadow-md rounded-lg p-4 col-span-1 h-[280px] w-auto flex flex-col">
                  <h2 className="text-md mb-2 font-bold">청첩장 장부 차트</h2>
                  <div className="flex-grow h-[220px]">
                    <Line
                      data={chartData}
                      options={{
                        ...chartOptions,
                        maintainAspectRatio: false,
                      }}
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        ) : (
          ""
        )}
      </div>
    </div>
  );
};

export default AccountBook;

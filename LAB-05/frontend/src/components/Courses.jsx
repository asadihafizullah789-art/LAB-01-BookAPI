import { useEffect, useState } from "react";
import { getCourses } from "../services/courseApi";

export default function Courses() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        let ignore = false;

        async function fetchCourses() {
            try {
                const response = await getCourses();
                if (!ignore) setCourses(response.data);
            } catch (err) {
                if (!ignore) setError("Unable to load courses. Check the backend and try again.");
            } finally {
                if (!ignore) setLoading(false);
            }
        }

        fetchCourses();
        return () => { ignore = true; };
    }, []);

    return (
        <section style={{ padding: "20px", fontFamily: "sans-serif" }}>
            <h2>Course Management</h2>

            {loading && <p>Loading courses...</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}
            {!loading && !error && courses.length === 0 && <p>No courses available.</p>}

            {!loading && !error && courses.length > 0 && (
                <>
                    <p><strong>Total Courses: {courses.length}</strong></p>
                    <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "10px" }}>
                        <thead>
                        <tr style={{ background: "#163c64", color: "white" }}>
                            <th style={tableStyle}>ID</th>
                            <th style={tableStyle}>Code</th>
                            <th style={tableStyle}>Title</th>
                            <th style={tableStyle}>Credits</th>
                        </tr>
                        </thead>
                        <tbody>
                        {courses.map((course) => (
                            <tr key={course.id}>
                                <td style={tableStyle}>{course.id}</td>
                                <td style={tableStyle}>{course.code}</td>
                                <td style={tableStyle}>{course.title}</td>
                                <td style={tableStyle}>{course.credits}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </>
            )}
        </section>
    );
}

const tableStyle = { border: "1px solid #ccc", padding: "10px", textAlign: "left" };